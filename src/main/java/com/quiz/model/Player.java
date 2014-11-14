package com.quiz.model;

public class Player {
	
	String player;
	int playerNumber;
	boolean validPlayer;
	boolean playerReady;
	int playerNumCorrect;
	int playerNumWrong;
	int playerNumNoAnswer;
	double playerPoints;
	boolean playerCurrQDone;
	
	public Player(){
		this.validPlayer = false;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public boolean isValidPlayer() {
		return validPlayer;
	}
	public void setValidPlayer(boolean validPlayer) {
		this.validPlayer = validPlayer;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public boolean isPlayerReady() {
		return playerReady;
	}
	public void setPlayerReady(boolean playerReady) {
		this.playerReady = playerReady;
	}
	public int getPlayerNumCorrect() {
		return playerNumCorrect;
	}
	public void setPlayerNumCorrect(int playerNumCorrect) {
		this.playerNumCorrect = playerNumCorrect;
	}
	public int getPlayerNumWrong() {
		return playerNumWrong;
	}
	public void setPlayerNumWrong(int playerNumWrong) {
		this.playerNumWrong = playerNumWrong;
	}
	public int getPlayerNumNoAnswer() {
		return playerNumNoAnswer;
	}
	public void setPlayerNumNoAnswer(int playerNumNoAnswer) {
		this.playerNumNoAnswer = playerNumNoAnswer;
	}
	public double getPlayerPoints() {
		return playerPoints;
	}
	public void setPlayerPoints(double playerPoints) {
		this.playerPoints = playerPoints;
	}
	public boolean isPlayerCurrQDone() {
		return playerCurrQDone;
	}
	public void setPlayerCurrQDone(boolean playerCurrQDone) {
		this.playerCurrQDone = playerCurrQDone;
	}
	public int getPlayerNumber() {
		return playerNumber;
	}
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	@Override
	public String toString() {
		return "Player [player=" + player + ", playerNumber=" + playerNumber
				+ ", validPlayer=" + validPlayer + ", playerReady="
				+ playerReady + ", playerNumCorrect=" + playerNumCorrect
				+ ", playerNumWrong=" + playerNumWrong + ", playerNumNoAnswer="
				+ playerNumNoAnswer + ", playerPoints=" + playerPoints
				+ ", playerCurrQDone=" + playerCurrQDone + "]";
	}

}
