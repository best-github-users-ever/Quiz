package com.quiz.socket.gamelogic;

import com.quiz.model.PlayerList;

public class GameStartResult {
	private PlayerList playerList;
	private String messageName;
    private String delayTime;
    public GameStartResult(String messageName, PlayerList playerList, String delayTime) {
    	this.playerList = playerList;
    	this.messageName = messageName;
        this.delayTime = delayTime;
    }
	public String getDelayTime() {
		return delayTime;
	}
	public String getMessageName() {
		return messageName;
	}
	public PlayerList getPlayerList() {
		return playerList;
	}
	
} 