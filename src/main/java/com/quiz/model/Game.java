package com.quiz.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class Game implements Serializable {
	/**
	 * 
	 */
	public static final int NUMBER_QUESTIONS_PER_GAME = 1;
	
	private static Logger log = Logger
			.getLogger(Game.class);
	
	private static final long serialVersionUID = 747083550811540580L;
	int gameId;
	int topicId;
	int totalPlayers;
	int numPlayers;
	int currQIndex;
	String player1;
	boolean p1Ready;
	int p1NumCorrect;
	double p1Time;
	boolean p1CurrQDone;
	String player2;
	boolean p2Ready;
	int p2NumCorrect;
	double p2Time;
	boolean p2CurrQDone;
	String player3;
	boolean p3Ready;
	int p3NumCorrect;
	double p3Time;
	boolean p3CurrQDone;
	String player4;
	boolean p4Ready;
	int p4NumCorrect;
	double p4Time;
	boolean p4CurrQDone;
	String player5;
	boolean p5Ready;
	int p5NumCorrect;
	double p5Time;
	boolean p5CurrQDone;
	
	public Game() {
		// TODO Auto-generated constructor stub
	}

	public boolean allPlayersFinishedQuestion(){

			if (getTotalPlayers() == 2) {
				return isP1CurrQDone() & isP2CurrQDone();
			} else if (getTotalPlayers() == 3) {
				return isP1CurrQDone() & isP2CurrQDone() & isP3CurrQDone();
			} else if (getTotalPlayers() == 4) {
				return isP1CurrQDone() & isP2CurrQDone() & isP3CurrQDone()
						& isP4CurrQDone();
			} else if (getTotalPlayers() == 5) {
				return isP1CurrQDone() & isP2CurrQDone() & isP3CurrQDone()
						& isP4CurrQDone() & isP5CurrQDone();
			} else if (getTotalPlayers() == 1) {
				return isP1CurrQDone();
			} else {
				log.info("value of total players is invalid in gameId: "
						+ this.getGameId());
				return false;
			}
		
	}
	
	public int getCurrQIndex() {
		return currQIndex;
	}

	public int getGameId() {
		return gameId;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getP1NumCorrect() {
		return p1NumCorrect;
	}

	public double getP1Time() {
		return p1Time;
	}

	public int getP2NumCorrect() {
		return p2NumCorrect;
	}

	public double getP2Time() {
		return p2Time;
	}

	public int getP3NumCorrect() {
		return p3NumCorrect;
	}

	public double getP3Time() {
		return p3Time;
	}

	public int getP4NumCorrect() {
		return p4NumCorrect;
	}

	public double getP4Time() {
		return p4Time;
	}

	public int getP5NumCorrect() {
		return p5NumCorrect;
	}

	public double getP5Time() {
		return p5Time;
	}

	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public String getPlayer3() {
		return player3;
	}

	public String getPlayer4() {
		return player4;
	}

	public String getPlayer5() {
		return player5;
	}


	public int getTopicId() {
		return topicId;
	}

	public int getTotalPlayers() {
		return totalPlayers;
	}

	public boolean isP1CurrQDone() {
		return p1CurrQDone;
	}

	public boolean isP1Ready() {
		return p1Ready;
	}

	public boolean isP2CurrQDone() {
		return p2CurrQDone;
	}

	public boolean isP2Ready() {
		return p2Ready;
	}

	public boolean isP3CurrQDone() {
		return p3CurrQDone;
	}

	public boolean isP3Ready() {
		return p3Ready;
	}

	public boolean isP4CurrQDone() {
		return p4CurrQDone;
	}

	public boolean isP4Ready() {
		return p4Ready;
	}

	public boolean isP5CurrQDone() {
		return p5CurrQDone;
	}

	public boolean isP5Ready() {
		return p5Ready;
	}

	public void setCurrQIndex(int currQIndex) {
		this.currQIndex = currQIndex;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public void setP1CurrQDone(boolean p1CurrQDone) {
		this.p1CurrQDone = p1CurrQDone;
	}

	public void setP1NumCorrect(int p1NumCorrect) {
		this.p1NumCorrect = p1NumCorrect;
	}

	public void setP1Ready(boolean p1Ready) {
		this.p1Ready = p1Ready;
	}

	public void setP1Time(double p1Time) {
		this.p1Time = p1Time;
	}

	public void setP2CurrQDone(boolean p2CurrQDone) {
		this.p2CurrQDone = p2CurrQDone;
	}

	public void setP2NumCorrect(int p2NumCorrect) {
		this.p2NumCorrect = p2NumCorrect;
	}

	public void setP2Ready(boolean p2Ready) {
		this.p2Ready = p2Ready;
	}

	public void setP2Time(double p2Time) {
		this.p2Time = p2Time;
	}

	public void setP3CurrQDone(boolean p3CurrQDone) {
		this.p3CurrQDone = p3CurrQDone;
	}

	public void setP3NumCorrect(int p3NumCorrect) {
		this.p3NumCorrect = p3NumCorrect;
	}

	public void setP3Ready(boolean p3Ready) {
		this.p3Ready = p3Ready;
	}

	public void setP3Time(double p3Time) {
		this.p3Time = p3Time;
	}

	public void setP4CurrQDone(boolean p4CurrQDone) {
		this.p4CurrQDone = p4CurrQDone;
	}

	public void setP4NumCorrect(int p4NumCorrect) {
		this.p4NumCorrect = p4NumCorrect;
	}

	public void setP4Ready(boolean p4Ready) {
		this.p4Ready = p4Ready;
	}

	public void setP4Time(double p4Time) {
		this.p4Time = p4Time;
	}

	public void setP5CurrQDone(boolean p5CurrQDone) {
		this.p5CurrQDone = p5CurrQDone;
	}

	public void setP5NumCorrect(int p5NumCorrect) {
		this.p5NumCorrect = p5NumCorrect;
	}

	public void setP5Ready(boolean p5Ready) {
		this.p5Ready = p5Ready;
	}

	public void setP5Time(double p5Time) {
		this.p5Time = p5Time;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public void setPlayer3(String player3) {
		this.player3 = player3;
	}

	public void setPlayer4(String player4) {
		this.player4 = player4;
	}

	public void setPlayer5(String player5) {
		this.player5 = player5;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}

}
