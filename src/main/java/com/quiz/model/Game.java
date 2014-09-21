package com.quiz.model;

import java.io.Serializable;

public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 747083550811540580L;
	int gameId;
	int topicId;
	int totalPlayers;
	int numPlayers;
	String player1;
	String player2;
	String player3;
	String player4;
	String player5;
	
	public Game() {
		// TODO Auto-generated constructor stub
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getTotalPlayers() {
		return totalPlayers;
	}

	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public String getPlayer3() {
		return player3;
	}

	public void setPlayer3(String player3) {
		this.player3 = player3;
	}

	public String getPlayer4() {
		return player4;
	}

	public void setPlayer4(String player4) {
		this.player4 = player4;
	}

	public String getPlayer5() {
		return player5;
	}

	public void setPlayer5(String player5) {
		this.player5 = player5;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", topicId=" + topicId
				+ ", totalPlayers=" + totalPlayers + ", numPlayers="
				+ numPlayers + ", player1=" + player1 + ", player2=" + player2
				+ ", player3=" + player3 + ", player4=" + player4
				+ ", player5=" + player5 + "]";
	}
	
	

}
