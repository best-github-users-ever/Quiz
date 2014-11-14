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
	List<Player> players;
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

	public Game() {

		Player player;
		players = new ArrayList<Player>();

		for (int i = 1; i <= Game.MAX_PLAYERS_PER_GAME; i++) {
			player = new Player();
			player.setPlayerNumber(i);
			players.add(player);
		}
	}

	public void dump_if_discrepancy() {

		Player player;

		for (int i = 1; i <= Game.MAX_PLAYERS_PER_GAME; i++) {
			player = getPlayerFromPlayerNumber(i);

			if (player.isValidPlayer()) {
				if (player.isPlayerCurrQDone()) {
					if (player.getPlayerNumCorrect()
							+ player.getPlayerNumWrong()
							+ player.getPlayerNumNoAnswer() != currQIndex) {
						try {
							log.info(this.toString());
							throw new Exception("Tracing only");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public boolean allPlayersFinishedQuestion() {

		for (Player player : players) {

			if (player.isValidPlayer()) {

				if (!player.isPlayerCurrQDone()) {
					return false;
				}
			}
		}

		return true;
	}

	public List<String> getAllPlayerUserIds() {
		List<String> playerList = new ArrayList<String>();

		for (Player player : players) {

			if (player.isValidPlayer()) {

				playerList.add(player.getPlayer());
			}
		}
		if (playerList.isEmpty()) {
			return null;
		} else {
			return playerList;
		}
	}

	public List<String> getOtherPlayerUserIds(String userId) {
		List<String> playerList = new ArrayList<String>();

		for (Player player : players) {

			if (player.isValidPlayer() && !player.getPlayer().equals(userId)) {

				playerList.add(player.getPlayer());
			}
		}
		if (playerList.isEmpty()) {
			return null;
		} else {
			return playerList;
		}
	}

	public boolean allPlayersReady() {

		for (Player player : players) {

			if (player.isValidPlayer()) {

				if (!player.isPlayerReady()) {
					return false;
				}
			}
		}

		return true;
	}

	public String determineHigherScore(String playerAName,
			List<String> playerBList, Map<String, String> equivalencyMap) {

		if (playerBList.size() > 1) {
			String playerBName = playerBList.get(0);
			playerBList.remove(0);

			String localWinner;

			Player playerA = findPlayerFromUserId(playerAName);
			Player playerB = findPlayerFromUserId(playerBName);

			if (playerA == null) {
				log.info("incorrect Player A (" + playerAName + ") entered!");
				return "";
			} else if (playerB == null) {
				log.info("incorrect Player B (" + playerBName + ") entered!");
				return "";
			}

			int playerANumWrong = playerA.getPlayerNumWrong();
			int playerBNumWrong = playerB.getPlayerNumWrong();

			double playerAPoints = playerA.getPlayerPoints();
			double playerBPoints = playerB.getPlayerPoints();

			// use points to determine winner and fewer wrong to break ties.
			if (playerAPoints > playerBPoints) {
				localWinner = playerAName;
			} else if (playerAPoints < playerBPoints) {
				localWinner = playerBName;
			} else {
				if (playerANumWrong < playerBNumWrong) {
					localWinner = playerAName;
				} else if (playerANumWrong > playerBNumWrong) {
					localWinner = playerBName;
				} else {
					equivalencyMap.put(playerBName, playerAName);
					localWinner = playerBName;
				}
			}
			return determineHigherScore(localWinner, playerBList,
					equivalencyMap);

		} else {
			String playerBName = playerBList.get(0);

			Player playerA = findPlayerFromUserId(playerAName);
			Player playerB = findPlayerFromUserId(playerBName);

			if (playerA == null) {
				log.info("incorrect Player A (" + playerAName + ") entered!");
				return "";
			} else if (playerB == null) {
				log.info("incorrect Player B (" + playerBName + ") entered!");
				return "";
			}

			int playerANumWrong = playerA.getPlayerNumWrong();
			int playerBNumWrong = playerB.getPlayerNumWrong();

			double playerAPoints = playerA.getPlayerPoints();
			double playerBPoints = playerB.getPlayerPoints();

			// use points to determine winner and fewer wrong to break ties.
			if (playerAPoints > playerBPoints) {
				return playerAName;
			} else if (playerAPoints < playerBPoints) {
				return playerBName;
			} else {
				if (playerANumWrong < playerBNumWrong) {
					return playerAName;
				} else if (playerANumWrong > playerBNumWrong) {
					return playerBName;
				} else {
					equivalencyMap.put(playerBName, playerAName);
					return playerBName;
				}

			}
		}
	}

	public List<String> determineWinner() {

		List<String> winnersList;
		List<String> playerList;
		String playerAName;
		String winner;
		Map<String, String> equivalencyMap = new HashMap<String, String>();

		if (this.getCurrQIndex() != Game.NUMBER_QUESTIONS_PER_GAME) {
			return null;
		}

		playerList = getAllPlayerUserIds();
		log.info(playerList);

		if (playerList.size() > 1) {
			playerAName = playerList.get(0);
			playerList.remove(0);
			winner = determineHigherScore(playerAName, playerList,
					equivalencyMap);
			winnersList = Game.findAllEquivalents(winner, equivalencyMap);
			return winnersList;
		} else {
			return playerList;
		}
	}

	public static List<String> findAllEquivalents(String userId,
			Map<String, String> equivalencyMap) {

		List<String> fullList = new ArrayList<String>();
		List<String> tempList;
		String result = null;

		// start with the incoming userId
		fullList.add(userId);

		result = equivalencyMap.get(userId);
		if (result == null) {
			return fullList;
		} else {
			tempList = Game.findAllEquivalents(result, equivalencyMap);
			for (String tempUserId : tempList) {
				fullList.add(tempUserId);
			}

			return fullList;
		}
	}

	public Player findPlayerFromUserId(String userId) {

		for (Player player : players) {
			if (userId.equals(player.getPlayer())) {
				return player;
			}
		}

		log.info("invalid UserId (" + userId
				+ ") passed into findPlayerNumberFromUserId!");
		return null;

	}

	public int findPlayerNumberFromUserId(String userId) {

		for (Player player : players) {
			if (userId.equals(player.getPlayer())) {
				return player.getPlayerNumber();
			}
		}

		log.info("invalid UserId (" + userId
				+ ") passed into findPlayerNumberFromUserId!");
		return 0;

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

	public int getTopicId() {
		return topicId;
	}

	public int getTotalPlayers() {
		return totalPlayers;
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

	public Player getPlayerFromPlayerNumber(int playerNumber) {
		for (Player player : players) {
			if (player.getPlayerNumber() == playerNumber) {
				return player;
			}
		}

		return null;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", topicId=" + topicId
				+ ", totalPlayers=" + totalPlayers + ", numPlayers="
				+ numPlayers + ", currQIndex=" + currQIndex + ", q1Id=" + q1Id
				+ ", q2Id=" + q2Id + ", q3Id=" + q3Id + ", q4Id=" + q4Id
				+ ", q5Id=" + q5Id + ", q6Id=" + q6Id + ", q7Id=" + q7Id
				+ ", q8Id=" + q8Id + ", q9Id=" + q9Id + ", q10Id=" + q10Id
				+ ", players=" + players + "]";
	}

}
