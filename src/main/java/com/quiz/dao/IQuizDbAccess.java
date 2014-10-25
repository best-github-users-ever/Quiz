package com.quiz.dao;

import java.util.List;

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

public interface IQuizDbAccess {

	Game addGame(int topicId, int totalPlayers);

	boolean addUser(User u);

	boolean allPlayersReady(int gameId);

	Game findGameForNewPlayer(int topicId, int totalPlayers, String username);

	Question getQuestion(int topicId);

	Question getQuestionFromQuestionId(int questionId);

	Question getRandomQuestion(int topicId);
	
	List<String> getOtherPlayerUserIds(int gameId, String userId);

	User getUser(User user);

	Game joinGame(int gameId, String username);

	Game retrieveGamefromId(int gameId);

	Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers);

	Game setPlayerReady(String username, Game inputGame);

	String showHint(String userId);

}
