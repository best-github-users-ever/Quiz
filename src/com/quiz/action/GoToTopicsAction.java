package com.quiz.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Question;
import com.quiz.model.User;

public class GoToTopicsAction extends BaseAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6850123748364262650L;
	
	private static Logger log = Logger.getLogger(GoToTopicsAction.class);


	public GoToTopicsAction() {

	}

	
	@Override
	public String executeAction() {

		log.error("in GoToTopicsAction::in execute.");
		
        return SUCCESS;
	}



}
