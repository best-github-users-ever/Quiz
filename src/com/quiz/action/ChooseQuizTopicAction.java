package com.quiz.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

public class ChooseQuizTopicAction extends BaseAction implements Serializable {

	/**
	 * 
	 */
	private int topicId;
	private int numberPlayers;

	private static Logger log = Logger.getLogger(ChooseQuizTopicAction.class);

	private static final long serialVersionUID = 3522866317711753644L;

	public ChooseQuizTopicAction() {

	}

	@Override
	public String executeAction() {

		log.error("in LoginAction::in execute.");
		log.error("topicid:" + topicId);
		ActionContext context = ActionContext.getContext();

		IQuizDbAccess dao = DBAccess.getDbAccess();

		int numberPlayers = getNumberPlayers();

		getSession().put("numberPlayers", numberPlayers);
/*
		if (numberPlayers == 1) {

			// need to 'randomly' get a question from the topic.
			Question question = dao.getQuestion(getTopicId());

			if (question != null) {

				getSession().put("question", question);

				return SUCCESS;
			} else {
				// no questions for topic

				getRequest().put("reqErrorMessage",
						"No Questions available for this topic");
				return ERROR;
			}
		} else {
		*/
			Game game = null;

			game = dao.findGameForNewPlayer(getTopicId(), getNumberPlayers(),
					((User) getSession().get("user")).getUserId());

			if (game != null) {

				if (game.getTotalPlayers() == game.getNumPlayers()) {
					// temporary code printed as error msg
					getRequest().put("reqErrorMessage", "All Players found");
					// need to 'randomly' get a question from the topic.
					Question question = dao.getQuestion(getTopicId());

					if (question != null) {

						getSession().put("question", question);

						return SUCCESS;
					} else {
						// no questions for topic

						getRequest().put("reqErrorMessage",
								"No Questions available for this topic");
						return ERROR;
					}

				}
				else {
					// temporary code printed as error msg
					getRequest().put("reqErrorMessage",
							"Waiting for other players");
					return ERROR;
				}
			} else {
				getRequest()
						.put("reqErrorMessage",
								"Error finding a game for selected Topic and number of players");
				return ERROR;
			}

		//}
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getNumberPlayers() {
		return numberPlayers;
	}

	public void setNumberPlayers(int numberPlayers) {
		this.numberPlayers = numberPlayers;
	}

}
