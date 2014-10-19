package com.quiz.socket.gamelogic;
public class GameReadyResult {
	private String messageName;
    private String result;
    public GameReadyResult(String messageName, String result) {
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