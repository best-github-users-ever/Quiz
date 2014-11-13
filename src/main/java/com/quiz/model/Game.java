package com.quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Game implements Serializable {
	/**
	 * 
	 */
	public static final int NUMBER_QUESTIONS_PER_GAME = 10;
	public static final int MAX_PLAYERS_PER_GAME = 5;

	private static Logger log = Logger.getLogger(Game.class);

	private static final long serialVersionUID = 747083550811540580L;
	int gameId;
	int topicId;
	int totalPlayers;
	int numPlayers;
	int currQIndex;
	int q1Id;
	int q2Id;
	int q3Id;
	int q4Id;
	int q5Id;
	int q6Id;
	int q7Id;
	int q8Id;
	int q9Id;
	int q10Id;
	String player1;
	boolean p1Ready;
	int p1NumCorrect;
	int p1NumWrong;
	int p1NumNoAnswer;
	double p1Time;
	boolean p1CurrQDone;
	String player2;
	boolean p2Ready;
	int p2NumCorrect;
	int p2NumWrong;
	int p2NumNoAnswer;
	double p2Time;
	boolean p2CurrQDone;
	String player3;
	boolean p3Ready;
	int p3NumCorrect;
	int p3NumWrong;
	int p3NumNoAnswer;
	double p3Time;
	boolean p3CurrQDone;
	String player4;
	boolean p4Ready;
	int p4NumCorrect;
	int p4NumWrong;
	int p4NumNoAnswer;
	double p4Time;
	boolean p4CurrQDone;
	String player5;
	boolean p5Ready;
	int p5NumCorrect;
	int p5NumWrong;
	int p5NumNoAnswer;
	double p5Time;
	boolean p5CurrQDone;

	public Game() {
		// TODO Auto-generated constructor stub
	}

	public void dump_if_discrepancy() {

		if (p1CurrQDone) {
			if (p1NumCorrect + p1NumWrong + p1NumNoAnswer != currQIndex) {
				try {
					log.info(this.toString());
					throw new Exception("Tracing only");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (p2CurrQDone) {
			if (p2NumCorrect + p2NumWrong + p2NumNoAnswer != currQIndex) {
				try {
					log.info(this.toString());
					throw new Exception("Tracing only");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean allPlayersFinishedQuestion() {

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
	
	public List<String> determineHigherScore(String playerA, String playerB){
		List<String> returnList = new ArrayList<String>();
		int playerANumCorrect,
		    playerBNumCorrect;
		double playerATime,
		       playerBTime;
		
		switch (findPlayerNumberFromUserId(playerA)) {
		case 1:
			playerANumCorrect = getP1NumCorrect();
			playerATime = getP1Time();
			break;

		case 2:
			playerANumCorrect = getP2NumCorrect();
			playerATime = getP2Time();
			break;

		case 3:
			playerANumCorrect = getP3NumCorrect();
			playerATime = getP3Time();
			break;

		case 4:
			playerANumCorrect = getP4NumCorrect();
			playerATime = getP4Time();
			break;

		case 5:
			playerANumCorrect = getP5NumCorrect();
			playerATime = getP5Time();
			break;

		default:
			log.info("incorrect Player A (" + playerA + ") entered!");
			return null;
		}  
		
		switch (findPlayerNumberFromUserId(playerB)) {
		case 1:
			playerBNumCorrect = getP1NumCorrect();
			playerBTime = getP1Time();
			break;

		case 2:
			playerBNumCorrect = getP2NumCorrect();
			playerBTime = getP2Time();
			break;

		case 3:
			playerBNumCorrect = getP3NumCorrect();
			playerBTime = getP3Time();
			break;

		case 4:
			playerBNumCorrect = getP4NumCorrect();
			playerBTime = getP4Time();
			break;

		case 5:
			playerBNumCorrect = getP5NumCorrect();
			playerBTime = getP5Time();
			break;

		default:
			log.info("incorrect Player B (" + playerB + ") entered!");
			return null;
		}  
		
		if (playerANumCorrect > playerBNumCorrect){
			returnList.add(playerA);
			return returnList;
		} else if (playerANumCorrect < playerBNumCorrect){
			returnList.add(playerB);
			return returnList;
		} else {
			if (playerATime > playerBTime){
				returnList.add(playerB);
				return returnList;
			} else if (playerATime < playerBTime){
				returnList.add(playerA);
				return returnList;				
			} else {
				returnList.add(playerA);
				returnList.add(playerB);
				return returnList;				
				
			}
			
		}
	
	}

	public List<String> determineWinner(){

		// This algorithm depends on the first player argument (playerA) being added to the 
		// List returned by determineHigherScore before playerB in case of ties.
		
		List<String> winnersList = new ArrayList<String>();
		List<String> pairWinnerList; //pair winner lists are created by determineHigherScore
		Map<String, String> equivalencyMap = new HashMap<String, String>();
		
		if (this.getCurrQIndex() != Game.NUMBER_QUESTIONS_PER_GAME){
			return null;
		}
		
		if (getTotalPlayers() == 2) {
            winnersList = determineHigherScore(getPlayer1(), getPlayer2());

			return winnersList;
		} else if (getTotalPlayers() == 3) {

			pairWinnerList = determineHigherScore(getPlayer1(), getPlayer2());
			if (pairWinnerList.size() > 1){
				equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
				pairWinnerList = determineHigherScore(pairWinnerList.get(1), getPlayer3());
				if (pairWinnerList.size() > 1){
					equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
					winnersList = Game.findAllEquivalents(pairWinnerList.get(1), equivalencyMap);
					return winnersList;
				}
				else {
					winnersList = Game.findAllEquivalents(pairWinnerList.get(0), equivalencyMap);
					return winnersList;
				}
			} else {
				winnersList = determineHigherScore(pairWinnerList.get(0), getPlayer3());
				return winnersList;
			}
			
		} else if (getTotalPlayers() == 4) {
			pairWinnerList = determineHigherScore(getPlayer1(), getPlayer2());
			if (pairWinnerList.size() > 1){
				equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
				pairWinnerList = determineHigherScore(pairWinnerList.get(1), getPlayer3());
				if (pairWinnerList.size() > 1){
					equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
					pairWinnerList = determineHigherScore(pairWinnerList.get(1), getPlayer4());
					if (pairWinnerList.size() > 1){
						equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
						winnersList = Game.findAllEquivalents(pairWinnerList.get(1), equivalencyMap);
					    return winnersList;
					}
			    	else {
						winnersList = Game.findAllEquivalents(pairWinnerList.get(0), equivalencyMap);
					    return winnersList;
				    }
				} else {
					pairWinnerList = determineHigherScore(pairWinnerList.get(0), getPlayer4());
					if (pairWinnerList.size() > 1){
						equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
						winnersList = Game.findAllEquivalents(pairWinnerList.get(1), equivalencyMap);
					    return winnersList;
					} else {
				    	winnersList = Game.findAllEquivalents(pairWinnerList.get(0), equivalencyMap);
				        return winnersList;
					}
				}
									
			} else {
				pairWinnerList = determineHigherScore(pairWinnerList.get(0), getPlayer3());
				if (pairWinnerList.size() > 1){
					equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
					pairWinnerList = determineHigherScore(pairWinnerList.get(1), getPlayer4());
					if (pairWinnerList.size() > 1){
						equivalencyMap.put(pairWinnerList.get(1), pairWinnerList.get(0));
						winnersList = Game.findAllEquivalents(pairWinnerList.get(1), equivalencyMap);
					    return winnersList;
					}
			    	else {
						winnersList = Game.findAllEquivalents(pairWinnerList.get(0), equivalencyMap);
					    return winnersList;
				    }
				} else {
					winnersList = determineHigherScore(pairWinnerList.get(0), getPlayer4());
				    return winnersList;
				}
			}
			
		} else if (getTotalPlayers() == 5) {

			//will verify logic for 3 & 4 before adding 5
			return null;
			
		} else if (getTotalPlayers() == 1) {
			winnersList.add(getPlayer1());
			return winnersList;
		} else {
			log.info("value of total players is invalid in gameId: "
					+ this.getGameId());
			return null;
		}
	
	}
	
	public static List<String> findAllEquivalents(String userId, Map<String, String> equivalencyMap){
 
		List<String> fullList = new ArrayList<String>();
		List<String> tempList;
		String result = null;
		
		//start with the incoming userId
		fullList.add(userId);
		
		result = equivalencyMap.get(userId);
		if (result == null){
			return fullList;
		} else {
			tempList = Game.findAllEquivalents(result, equivalencyMap);
			for (String tempUserId : tempList){
				fullList.add(tempUserId);
			}
			
			return fullList;
		}
	}
	
	public int findPlayerNumberFromUserId(String userId){
		if (userId.equals(getPlayer1())) {
			return 1;
		} else if (userId.equals(getPlayer2())) {
			return 2;
		} else if (userId.equals(getPlayer3())) {
			return 3;
		} else if (userId.equals(getPlayer4())) {
			return 4;
		} else if (userId.equals(getPlayer5())) {
			return 5;
		} else {
			log.info("invalid UserId ("+ userId +") passed into findPlayerNumberFromUserId!");
			return 0;
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

	public int getP1NumNoAnswer() {
		return p1NumNoAnswer;
	}

	public int getP1NumWrong() {
		return p1NumWrong;
	}

	public double getP1Time() {
		return p1Time;
	}

	public int getP2NumCorrect() {
		return p2NumCorrect;
	}

	public int getP2NumNoAnswer() {
		return p2NumNoAnswer;
	}

	public int getP2NumWrong() {
		return p2NumWrong;
	}

	public double getP2Time() {
		return p2Time;
	}

	public int getP3NumCorrect() {
		return p3NumCorrect;
	}

	public int getP3NumNoAnswer() {
		return p3NumNoAnswer;
	}

	public int getP3NumWrong() {
		return p3NumWrong;
	}

	public double getP3Time() {
		return p3Time;
	}

	public int getP4NumCorrect() {
		return p4NumCorrect;
	}

	public int getP4NumNoAnswer() {
		return p4NumNoAnswer;
	}

	public int getP4NumWrong() {
		return p4NumWrong;
	}

	public double getP4Time() {
		return p4Time;
	}

	public int getP5NumCorrect() {
		return p5NumCorrect;
	}

	public int getP5NumNoAnswer() {
		return p5NumNoAnswer;
	}

	public int getP5NumWrong() {
		return p5NumWrong;
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

	public void setP1NumNoAnswer(int p1NumNoAnswer) {
		this.p1NumNoAnswer = p1NumNoAnswer;
	}

	public void setP1NumWrong(int p1NumWrong) {
		this.p1NumWrong = p1NumWrong;
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

	public void setP2NumNoAnswer(int p2NumNoAnswer) {
		this.p2NumNoAnswer = p2NumNoAnswer;
	}

	public void setP2NumWrong(int p2NumWrong) {
		this.p2NumWrong = p2NumWrong;
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

	public void setP3NumNoAnswer(int p3NumNoAnswer) {
		this.p3NumNoAnswer = p3NumNoAnswer;
	}

	public void setP3NumWrong(int p3NumWrong) {
		this.p3NumWrong = p3NumWrong;
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

	public void setP4NumNoAnswer(int p4NumNoAnswer) {
		this.p4NumNoAnswer = p4NumNoAnswer;
	}

	public void setP4NumWrong(int p4NumWrong) {
		this.p4NumWrong = p4NumWrong;
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

	public void setP5NumNoAnswer(int p5NumNoAnswer) {
		this.p5NumNoAnswer = p5NumNoAnswer;
	}

	public void setP5NumWrong(int p5NumWrong) {
		this.p5NumWrong = p5NumWrong;
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
	
	

	public int getQ1Id() {
		return q1Id;
	}

	public void setQ1Id(int q1Id) {
		this.q1Id = q1Id;
	}

	public int getQ2Id() {
		return q2Id;
	}

	public void setQ2Id(int q2Id) {
		this.q2Id = q2Id;
	}

	public int getQ3Id() {
		return q3Id;
	}

	public void setQ3Id(int q3Id) {
		this.q3Id = q3Id;
	}

	public int getQ4Id() {
		return q4Id;
	}

	public void setQ4Id(int q4Id) {
		this.q4Id = q4Id;
	}

	public int getQ5Id() {
		return q5Id;
	}

	public void setQ5Id(int q5Id) {
		this.q5Id = q5Id;
	}

	public int getQ6Id() {
		return q6Id;
	}

	public void setQ6Id(int q6Id) {
		this.q6Id = q6Id;
	}

	public int getQ7Id() {
		return q7Id;
	}

	public void setQ7Id(int q7Id) {
		this.q7Id = q7Id;
	}

	public int getQ8Id() {
		return q8Id;
	}

	public void setQ8Id(int q8Id) {
		this.q8Id = q8Id;
	}

	public int getQ9Id() {
		return q9Id;
	}

	public void setQ9Id(int q9Id) {
		this.q9Id = q9Id;
	}

	public int getQ10Id() {
		return q10Id;
	}

	public void setQ10Id(int q10Id) {
		this.q10Id = q10Id;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", topicId=" + topicId
				+ ", totalPlayers=" + totalPlayers + ", numPlayers="
				+ numPlayers + ", currQIndex=" + currQIndex + ", q1Id=" + q1Id
				+ ", q2Id=" + q2Id + ", q3Id=" + q3Id + ", q4Id=" + q4Id
				+ ", q5Id=" + q5Id + ", q6Id=" + q6Id + ", q7Id=" + q7Id
				+ ", q8Id=" + q8Id + ", q9Id=" + q9Id + ", q10Id=" + q10Id
				+ ", player1=" + player1 + ", p1Ready=" + p1Ready
				+ ", p1NumCorrect=" + p1NumCorrect + ", p1NumWrong="
				+ p1NumWrong + ", p1NumNoAnswer=" + p1NumNoAnswer + ", p1Time="
				+ p1Time + ", p1CurrQDone=" + p1CurrQDone + ", player2="
				+ player2 + ", p2Ready=" + p2Ready + ", p2NumCorrect="
				+ p2NumCorrect + ", p2NumWrong=" + p2NumWrong
				+ ", p2NumNoAnswer=" + p2NumNoAnswer + ", p2Time=" + p2Time
				+ ", p2CurrQDone=" + p2CurrQDone + ", player3=" + player3
				+ ", p3Ready=" + p3Ready + ", p3NumCorrect=" + p3NumCorrect
				+ ", p3NumWrong=" + p3NumWrong + ", p3NumNoAnswer="
				+ p3NumNoAnswer + ", p3Time=" + p3Time + ", p3CurrQDone="
				+ p3CurrQDone + ", player4=" + player4 + ", p4Ready=" + p4Ready
				+ ", p4NumCorrect=" + p4NumCorrect + ", p4NumWrong="
				+ p4NumWrong + ", p4NumNoAnswer=" + p4NumNoAnswer + ", p4Time="
				+ p4Time + ", p4CurrQDone=" + p4CurrQDone + ", player5="
				+ player5 + ", p5Ready=" + p5Ready + ", p5NumCorrect="
				+ p5NumCorrect + ", p5NumWrong=" + p5NumWrong
				+ ", p5NumNoAnswer=" + p5NumNoAnswer + ", p5Time=" + p5Time
				+ ", p5CurrQDone=" + p5CurrQDone + "]";
	}

}
