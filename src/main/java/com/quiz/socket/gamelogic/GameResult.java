package com.quiz.socket.gamelogic;

import java.io.Serializable;

public class GameResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7245191197341357723L;
	
	private String messageName;
    private String result;
    public GameResult(String messageName, String result) {
    	this.messageName = messageName;
        this.result = result;
    }
	public String getResult() {
		return result;
	}
	public String getMessageName() {
		return messageName;
	}
} 