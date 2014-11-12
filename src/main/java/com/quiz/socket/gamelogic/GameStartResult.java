package com.quiz.socket.gamelogic;

import java.io.Serializable;

import com.quiz.model.PlayerList;

public class GameStartResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6495467282465626615L;
	
	private PlayerList playerList;
	private String messageName;
	private int totalNumberOfQuestions;
    private String delayTime;

    public GameStartResult(String messageName, PlayerList playerList, int totalNumberOfQuestions, String delayTime) {
    	this.playerList = playerList;
    	this.messageName = messageName;
    	this.totalNumberOfQuestions = totalNumberOfQuestions;
        this.delayTime = delayTime;
    }
	public String getDelayTime() {
		return delayTime;
	}
	public String getMessageName() {
		return messageName;
	}
	public int getTotalNumberOfQuestions() {
		return totalNumberOfQuestions;
	}
	public PlayerList getPlayerList() {
		return playerList;
	}
	
} 