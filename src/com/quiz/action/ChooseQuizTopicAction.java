package com.quiz.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Question;
import com.quiz.model.User;

public class ChooseQuizTopicAction extends BaseAction implements Serializable {

	/**
	 * 
	 */
    private int topicId;
		
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
		
		//need to 'randomly' get a question from the topic.
		Question question = dao.getQuestion(getTopicId());

		if (question != null) {

			getSession().put("question", question);

			return SUCCESS;
		} else {
			//no questions for topic

			getRequest().put("reqErrorMessage", "No Questions available for this topic");
			return ERROR;
		}
	}


	public int getTopicId() {
		return topicId;
	}


	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}



}
