package com.quiz.action;

import java.io.Serializable;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.User;

public class NewAccountAction extends BaseAction implements Serializable, ModelDriven {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2094757026154265864L;

	private User user = new User();

	@Override
	public String executeAction() {
		ActionContext context = ActionContext.getContext();
		
		//need to make sure confirm password matches 

		IQuizDbAccess dao = DBAccess.getDbAccess();

		dao.addUser(user);

		//commenting line below. will make login rather than have acct creation login for us
		//context.getSession().put("user", user);
		
		return SUCCESS;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return user;
	}

}
