package com.quiz.socket.controller;

import java.util.List;
import java.util.Map;

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
import com.quiz.socket.gamelogic.GameQuestionFinishedResult;
import com.quiz.socket.gamelogic.GameQuestionResult;
import com.quiz.socket.gamelogic.GameResult;
import com.quiz.socket.gamelogic.GameStartResult;
import com.quiz.socket.gamelogic.JoinInput;

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

	@MessageMapping("/joinGame/ready")
	public void readyGameMessage(JoinInput input) throws Exception {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = null;

		String userId = input.getUserId();
		int gameId = input.getGameId();

		Game game = dao.setPlayerReady(userId, gameId);

		// need to deal with game = null error here

		if (dao.allPlayersReady(game.getGameId())) {
			String delayTime = "5";
			
			GameStartResult delayMessage = null;

			List<String> opponentList = dao
					.getOtherPlayerUserIds(gameId, userId);
			

			log.info("userid:"+userId+" opponentList:" + opponentList);
			
			delayMessage = new GameStartResult("delayBeforeStart", new PlayerList(opponentList), delayTime);
			
			template.convertAndSend("/queue/" + gameId + "/" + userId
					+ "/gameUpdates", delayMessage);
			
			for (String otherPlayer: opponentList){
				List<String> remainingOpponentList = dao
						.getOtherPlayerUserIds(gameId, otherPlayer);
				
				delayMessage = new GameStartResult("delayBeforeStart", new PlayerList(remainingOpponentList), delayTime);
				
				template.convertAndSend("/queue/" + gameId + "/" + otherPlayer
						+ "/gameUpdates", delayMessage);
				
			}

			try {
				// sleep an extra second to allow for propagation delay
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			question = dao.getRandomQuestion(game.getTopicId());
			
			int newNumber = dao.incrementQuestionNumber(game.getGameId());
			
			GameQuestionResult result = new GameQuestionResult("question", newNumber,
					Game.NUMBER_QUESTIONS_PER_GAME, question);
			log.info("result:" + result.toString());

			template.convertAndSend("/topic/" + input.getGameId()
					+ "/gameUpdates", result);
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
		String message =  "User '" + guesser + "' just guessed!";

		GameResult result = new GameResult("playerGuessed", message);

		template.convertAndSend("/queue/" + gameId + "/" + recipient
				+ "/gameUpdates", result);

	}

	@MessageMapping("/joinGame/answer")
	public void answerQuestionMessage(AnswerInput input) throws Exception {
		IQuizDbAccess dao = DBAccess.getDbAccess();

// Not necessary to look up session ID but will probably be useful in other
// websocket methods.
//
//		HttpSession session = HttpSessionCollector.find(input.getJsessionId());

		List<String> opponentList = dao.getOtherPlayerUserIds(
				input.getGameId(), input.getUserId());

		Question question = dao
				.getQuestionFromQuestionId(input.getQuestionId());

		if (question != null) {
			Game game = null;

			if (question.getAnswerIdx() == input.getGuess()) {
				game =	dao.setPlayerCorrectAnswer(input.getUserId(), input.getGameId());
				
				GameResult result = new GameResult("answerRight", "Correct!");
				log.info("result:" + result);

				template.convertAndSend("/queue/" + input.getGameId() + "/"
						+ input.getUserId() + "/gameUpdates", result);

			} else {

				game =	dao.setPlayerFinishedQuestion(input.getUserId(), input.getGameId());
				
				GameResult result = new GameResult("answerWrong", "Wrong!");
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
			
			if (game.allPlayersFinishedQuestion()){
		    	
				Thread.sleep(1000);
				sendPlayerQuestionResultsMessage(game);
			
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
	
	public void sendPlayerQuestionResultsMessage(Game game){
		
		GameQuestionFinishedResult questionResultMessage = null;

		questionResultMessage = new GameQuestionFinishedResult("questionResults", game);
		
		template.convertAndSend("/topic/" + game.getGameId() 
				+ "/gameUpdates", questionResultMessage);
		

	}

}
