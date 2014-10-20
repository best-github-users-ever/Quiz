package com.quiz.model;

import java.io.Serializable;

public class Game implements Serializable {
	/**
	 * 
	 */
	public static final int NUMBER_QUESTIONS_PER_GAME = 10;
	
	private static final long serialVersionUID = 747083550811540580L;
	int gameId;
	int topicId;
	int totalPlayers;
	int numPlayers;
	String player1;
	boolean p1Ready;
	String player2;
	boolean p2Ready;
	String player3;
	boolean p3Ready;
	String player4;
	boolean p4Ready;
	String player5;
	boolean p5Ready;
	
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


	public boolean isP1Ready() {
		return p1Ready;
	}

	public void setP1Ready(boolean p1Ready) {
		this.p1Ready = p1Ready;
	}

	public boolean isP2Ready() {
		return p2Ready;
	}

	public void setP2Ready(boolean p2Ready) {
		this.p2Ready = p2Ready;
	}

	public boolean isP3Ready() {
		return p3Ready;
	}

	public void setP3Ready(boolean p3Ready) {
		this.p3Ready = p3Ready;
	}

	public boolean isP4Ready() {
		return p4Ready;
	}

	public void setP4Ready(boolean p4Ready) {
		this.p4Ready = p4Ready;
	}

	public boolean isP5Ready() {
		return p5Ready;
	}

	public void setP5Ready(boolean p5Ready) {
		this.p5Ready = p5Ready;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", topicId=" + topicId
				+ ", totalPlayers=" + totalPlayers + ", numPlayers="
				+ numPlayers + ", player1=" + player1 + ", p1Ready="
				+ p1Ready + ", player2=" + player2 + ", p2Ready=" + p2Ready
				+ ", player3=" + player3 + ", p3Ready=" + p3Ready
				+ ", player4=" + player4 + ", p4Ready=" + p4Ready
				+ ", player5=" + player5 + ", p5Ready=" + p5Ready + "]";
	}

	
	

}
