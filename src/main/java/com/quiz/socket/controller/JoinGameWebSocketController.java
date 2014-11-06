package com.quiz.socket.controller;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.listener.HttpSessionCollector;
import com.quiz.model.Game;
import com.quiz.model.PlayerList;
import com.quiz.model.Question;
import com.quiz.model.User;
import com.quiz.socket.gamelogic.AnswerInput;
import com.quiz.socket.gamelogic.ChatInput;
import com.quiz.socket.gamelogic.ChatResult;
import com.quiz.socket.gamelogic.GameFinishedResult;
import com.quiz.socket.gamelogic.GameQuestionFinishedResult;
import com.quiz.socket.gamelogic.GameQuestionResult;
import com.quiz.socket.gamelogic.GameResult;
import com.quiz.socket.gamelogic.GameStartResult;
import com.quiz.socket.gamelogic.JoinInput;
import com.quiz.utility.QuizTimerTask;

@Controller
public class JoinGameWebSocketController {
	private SimpMessagingTemplate template;

	private static Logger log = Logger
			.getLogger(JoinGameWebSocketController.class);

	@Autowired
	JoinGameWebSocketController(SimpMessagingTemplate template) {
		this.template = template;
	}

	public SimpMessagingTemplate getTemplate() {
		return template;
	}

	@MessageMapping("/joinGame/chat")
	public void chatMessage(ChatInput input) throws Exception {

		IQuizDbAccess dao = DBAccess.getDbAccess();
		ChatResult chatMessage;
		List<String> playerList = null;

		if ("public".equals(input.getMsgType())) {
			playerList = dao.getOtherPlayerUserIds(input.getGameId(),
					input.getUserId());

			if (playerList != null) {
				playerList.toString();
			}
		} else {
			playerList = input.getRecipients();
		}

		for (String otherPlayer : playerList) {

			chatMessage = new ChatResult("chat", input.getUserId(),
					input.getChatMessage(), input.getMsgType());

			template.convertAndSend("/queue/" + input.getGameId() + "/"
					+ otherPlayer + "/gameUpdates", chatMessage);

		}

	}

	@MessageMapping("/joinGame/ready")
	public void readyGameMessage(JoinInput input) throws Exception {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = null;

		String userId = input.getUserId();
		final int gameId = input.getGameId();

		Game game = dao.setPlayerReady(userId, gameId);

		// need to deal with game = null error here

		if (dao.allPlayersReady(game.getGameId())) {
			String delayTime = "5"; // seconds to delay before game

			GameStartResult delayMessage = null;

			List<String> playerList = dao.getAllPlayerUserIds(gameId);

			log.info("userid:" + userId + " playerList:" + playerList);

			delayMessage = new GameStartResult("delayBeforeStart",
					new PlayerList(playerList), Game.NUMBER_QUESTIONS_PER_GAME,
					delayTime);

			template.convertAndSend("/topic/" + gameId + "/gameUpdates",
					delayMessage);
			log.info("playerList" + playerList.toString());

			try {
				// sleep an extra second to allow for propagation delay
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			do {
				game = dao.retrieveGamefromId(gameId);

				if (!game.allPlayersFinishedQuestion()
						& game.getCurrQIndex() != 0) {
					Thread.sleep(1000);
					continue;
				}

				game = dao.resetPlayersQDone(game);
				
				//select question that hasn't yet been asked in this game
				question = dao.getRandomQuestionWithExclusions(
						game.getTopicId(), game.getGameId());

				// temporary until database size is increased (if category
				// doesn't have at least 10 questions)
				if (question == null) {
					log.info("All available questions have already been asked. This is a repeat question. Curr Index = "
							+ game.getCurrQIndex());
					question = dao.getRandomQuestion(game.getTopicId());
				}

				game = dao.updateQuestionInfo(game.getGameId(),
						question.getQuestionId());

				if (game.getCurrQIndex() > 1) {
					// delay after 1st question
					try {
						// Wait between questions
						Thread.sleep(3500);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}

				GameQuestionResult result = new GameQuestionResult("question",
						game.getCurrQIndex(), question);
				log.info("result:" + result.toString());

				template.convertAndSend("/topic/" + input.getGameId()
						+ "/gameUpdates", result);

				// set timer to 13 seconds. users will have 10
				Timer timer = new Timer();

				// pass in data needed by timer on expiration
				TimerTask task = new QuizTimerTask(gameId,
						game.getCurrQIndex(), template);

				timer.schedule(task, 13 * 1000);

			} while (game.getCurrQIndex() < Game.NUMBER_QUESTIONS_PER_GAME);

			if (game.getCurrQIndex() >= Game.NUMBER_QUESTIONS_PER_GAME) {

				log.info("***** end of game " + game.getGameId() + ". wait for results of last question****");
				// send end of game message
			}

		} else {
			GameResult result = new GameResult("continueWaiting",
					"Waiting for other player to be ready in order to begin!");
			log.info("result:" + result);

			template.convertAndSend("/queue/" + input.getGameId() + "/"
					+ userId + "/gameUpdates", result);

		}

	}

	public static void sendGameReadyMessageToOpponent(
			SimpMessagingTemplate templateIn, int gameId, String recipient) {
		String message = null;
		String opponentNameList = "";
		IQuizDbAccess dao = DBAccess.getDbAccess();
		List<String> opponentList = dao
				.getOtherPlayerUserIds(gameId, recipient);

		if (opponentList != null) {
			if (opponentList.size() > 1) {
				message = "Game with users ";

				for (String user : opponentList) {
					opponentNameList = opponentNameList + "'" + user + "' ";
				}

				message = message + opponentNameList + "can now begin!";

			} else {
				message = "Game with user '" + opponentList.get(0)
						+ "' can now begin!";
			}
		}

		GameResult result = new GameResult("gameReady", message);

		log.info("result:" + result);
		log.info("template is null:" + (templateIn == null));

		log.info("xxxxxx opponentList:" + opponentList);
		if (opponentList != null) {
			templateIn.convertAndSend("/queue/" + gameId + "/" + recipient
					+ "/gameUpdates", result);

		}

	}

	public void sendPlayerGuessedMessageToOpponent(int gameId, String guesser,
			String recipient) {
		log.info("guesser:" + guesser + " recipient:" + recipient);
		String message = "User '" + guesser + "' just guessed!";

		GameResult result = new GameResult("playerGuessed", message);

		template.convertAndSend("/queue/" + gameId + "/" + recipient
				+ "/gameUpdates", result);

	}

	@MessageMapping("/joinGame/answer")
	public void answerQuestionMessage(AnswerInput input) throws Exception {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		// Not necessary to look up session ID but will probably be useful in
		// other websocket methods.
		//
		// HttpSession session =
		// HttpSessionCollector.find(input.getJsessionId());

		List<String> opponentList = dao.getOtherPlayerUserIds(
				input.getGameId(), input.getUserId());

		Question question = dao
				.getQuestionFromQuestionId(input.getQuestionId());

		if (question != null) {
			Game game = null;

			if (question.getAnswerIdx() == input.getGuess()) {
				game = dao.setPlayerCorrectAnswer(input.getUserId(),
						input.getAnswerTime(), input.getGameId());
				GameResult result = null;

				if (game != null) {

					result = new GameResult("answerRight", "Correct!");
				} else {
					result = new GameResult("answerLate",
							"Answer arrived too late!");
				}

				log.info("result:" + result);

				template.convertAndSend("/queue/" + input.getGameId() + "/"
						+ input.getUserId() + "/gameUpdates", result);

			} else {

				game = dao.setPlayerWrongAnswer(input.getUserId(),
						input.getGameId());
				GameResult result = null;

				if (game != null) {

					result = new GameResult("answerWrong", "Wrong!");
				} else {
					result = new GameResult("answerLate",
							"Answer arrived too late!");
				}

				log.info("result:" + result);

				template.convertAndSend("/queue/" + input.getGameId() + "/"
						+ input.getUserId() + "/gameUpdates", result);
			}

			log.info("opponentList:" + opponentList);
			// send messages to other players
			for (String recipient : opponentList) {
				sendPlayerGuessedMessageToOpponent(input.getGameId(),
						input.getUserId(), recipient);
			}

			if (game == null) {
				// late answer. just retrieve the game again.
				// inefficient but this is a rare event.
				game = dao.retrieveGamefromId(input.getGameId());
			}

			if (game.allPlayersFinishedQuestion()
					&& (game.getCurrQIndex() == Game.NUMBER_QUESTIONS_PER_GAME)) {

				sendPlayersGameResultsMessage(template, game);

			} else if (game.allPlayersFinishedQuestion()) {
				sendPlayersQuestionResultsMessage(template, game);
			}

		} else {
			// no questions for topic

			GameResult result = new GameResult("answerResult",
					"Error! QuestionId" + input.getQuestionId()
							+ " was not found!");
			log.info("result:" + result);

			template.convertAndSend(
					"/queue/" + input.getGameId() + "/" + input.getUserId()
							+ "/gameUpdates", result);
		}
	}

	public static void sendPlayersQuestionResultsMessage(
			SimpMessagingTemplate templateIn, Game game) {

		GameQuestionFinishedResult questionResultMessage = null;

		questionResultMessage = new GameQuestionFinishedResult(
				"questionResults", game);
		log.info("******GAME" + game.toString());

		templateIn.convertAndSend(
				"/topic/" + game.getGameId() + "/gameUpdates",
				questionResultMessage);

	}

	public static void sendPlayersGameResultsMessage(
			SimpMessagingTemplate templateIn, Game game) {

		GameFinishedResult gameResultMessage = null;

		gameResultMessage = new GameFinishedResult("gameResults", game,
				game.determineWinner());
		log.info("******GAME" + game.toString());

		templateIn.convertAndSend(
				"/topic/" + game.getGameId() + "/gameUpdates",
				gameResultMessage);

	}

}
