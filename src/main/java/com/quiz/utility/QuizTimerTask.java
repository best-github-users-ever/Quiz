package com.quiz.utility;

import java.io.Serializable;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Game;
import com.quiz.socket.controller.JoinGameWebSocketController;

public class QuizTimerTask extends TimerTask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8421205880747739248L;

	private static Logger log = Logger
			.getLogger(JoinGameWebSocketController.class);

	int gameId;
	int theTimersQuestionId;
	SimpMessagingTemplate template;

	public QuizTimerTask(int gameId, int theTimersQuestionId,
			SimpMessagingTemplate template) {
		this.gameId = gameId;
		this.theTimersQuestionId = theTimersQuestionId;
		this.template = template;
	}

	@Override
	public void run() {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		Game game = dao.retrieveGamefromId(gameId);
		log.info("game in timer:" + game);
		log.info("saved data" + this);

		if ((!dao.allPlayersFinishedQuestion(gameId))
				& (game.getCurrQIndex() == theTimersQuestionId)) {

			game = dao.setRemainingPlayersNoAnswer(gameId);

			if (game == null) {
				// all answered prior to expiration.
				// results have already been sent out so just return.
				return;
			}

			if (game.allPlayersFinishedQuestion()
					&& (game.getCurrQIndex() == Game.NUMBER_QUESTIONS_PER_GAME)) {

				JoinGameWebSocketController.sendPlayersGameResultsMessage(template, game);

			} else {
				
				JoinGameWebSocketController.sendPlayersQuestionResultsMessage(
						template, game);
			}

		}

	}

	@Override
	public String toString() {
		return "QuizTimerTask [gameId=" + gameId + ", theTimersQuestionId="
				+ theTimersQuestionId + ", template=" + template + "]";
	}

}