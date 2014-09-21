package com.quiz.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Question;
import com.quiz.model.User;

public class AnswerQuestionAction extends BaseAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8071764397649917385L;

	private static Logger log = Logger.getLogger(AnswerQuestionAction.class);

	private int option;

	public AnswerQuestionAction() {

	}

	@Override
	public String executeAction() {

		log.error("in AnswerQuestionAction::in execute.");
		log.error("option:" + option);
		ActionContext context = ActionContext.getContext();

		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = (Question) context.getSession().get("question");

		if (question != null) {

			if (question.getAnswerIdx() == option) {
				getRequest().put("reqPositiveMessage", "Correct!");

			} else {

				getRequest().put("reqErrorMessage", "Wrong!");
			}

			return SUCCESS;
		} else {
			// no questions for topic

			getRequest().put("reqErrorMessage", "Question Not Found");
			return ERROR;
		}
	}

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}
	
	

}
