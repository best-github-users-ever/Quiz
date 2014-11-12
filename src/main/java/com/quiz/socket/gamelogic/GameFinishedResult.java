package com.quiz.socket.gamelogic;

import java.io.Serializable;
import java.util.List;

import com.quiz.model.Game;

public class GameFinishedResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6361659436404111370L;
	private String messageName;
	private Game game;
	private List<String> winners;
    
    public GameFinishedResult(String messageName, Game game, List<String> winners) {
    	this.messageName = messageName;
        this.game = game;
        this.winners = winners;
    }

	public String getMessageName() {
		return messageName;
	}

	public Game getGame() {
		return game;
	}

	public List<String> getWinners() {
		return winners;
	}
	
	
} 