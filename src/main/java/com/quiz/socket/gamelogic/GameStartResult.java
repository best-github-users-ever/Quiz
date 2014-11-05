package com.quiz.socket.gamelogic;

import com.quiz.model.PlayerList;

public class GameStartResult {
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