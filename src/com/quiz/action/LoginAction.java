package com.quiz.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.User;

public class LoginAction extends BaseAction implements Serializable,
		ModelDriven {

	/**
	 * 
	 */
	private User user = new User();

	private static Logger log = Logger.getLogger(LoginAction.class);

	private static final long serialVersionUID = 3522866317711753644L;

	public LoginAction() {

	}

	@Override
	public String executeAction() {

		log.error("in LoginAction::in execute.");
		log.error(user.toString());
		ActionContext context = ActionContext.getContext();

		// ensure previous user is logged out
		getSession().remove("user");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		User localUser = dao.getUser(getUser());

		if (localUser != null) {

			getSession().put("user", localUser);

			return SUCCESS;
		} else {
			getSession().put("userName", getUser().getUserId().trim());

			getRequest().put("reqErrorMessage",
					"UserName Password combination is not correct");
			return ERROR;
		}
	}

	public User getUser() {
		return user;
	}

	public void setUserId(User user) {
		this.user = user;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return user;
	}

}
