package com.quiz.socket.gamelogic;
public class GameResult {
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