package com.quiz.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.quiz.action.ChooseQuizTopicAction;

public class User implements Serializable {
	
	private static final long serialVersionUID = 2274657357128107147L;

	private static Logger log = Logger.getLogger(User.class);

	private  String userId = null;
	private  String password = null;
	private  String hint = null;
	private String emailAddress = null;
		
	public User(){
		
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public void setUserId(String userName) {
		this.userId = userName;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setHint(String hint) {
		this.hint = hint;
	}


	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getHint() {
		return hint;
	}

	public boolean equals(String userName, String password) {
		return this.userId.equals(userName) && this.password.equals(password);
	}



	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password
				+ ", hint=" + hint + ", emailAddress=" + emailAddress + "]";
	}

}
