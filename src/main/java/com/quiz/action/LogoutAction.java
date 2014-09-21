package com.quiz.action;

import java.io.Serializable;



public class LogoutAction extends BaseAction implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7962913463026025113L;
	
	@Override
	public String executeAction() {
      
        getSession().remove("user");
		return SUCCESS;
    }

}


