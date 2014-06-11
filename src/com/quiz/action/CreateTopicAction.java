package com.quiz.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Question;
import com.quiz.model.User;

public class CreateTopicAction extends BaseAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6041792250991558880L;
	/**
	 * 
	 */
	private static Logger log = Logger.getLogger(CreateTopicAction.class);


	public CreateTopicAction() {

	}

	
	@Override
	public String executeAction() {

		log.error("in CreateTopicAction::in execute.");
		ActionContext context = ActionContext.getContext();

		IQuizDbAccess dao = DBAccess.getDbAccess();
		
        return SUCCESS;
	}



}
