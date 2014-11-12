package com.quiz.socket.gamelogic;

import java.io.Serializable;

import com.quiz.model.Game;

public class GameQuestionFinishedResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 789940727234124975L;
	
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