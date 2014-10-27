package com.quiz.socket.gamelogic;

import com.quiz.model.Game;

public class GameQuestionFinishedResult {
	private String messageName;
	private Game game;
    
    public GameQuestionFinishedResult(String messageName, Game game) {
    	this.messageName = messageName;
        this.game = game;
    }

	public String getMessageName() {
		return messageName;
	}

	public Game getGame() {
		return game;
	}
	
} 