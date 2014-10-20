package com.quiz.dao;

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

public interface IQuizDbAccess {
	
	boolean addUser(User u);
	
	String showHint(String userId);
	
	Game findGameForNewPlayer(int topicId, int totalPlayers, String username);
	
	Game addGame(int topicId, int totalPlayers);
	
	Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers);

	Game retrieveGamefromId(int gameId);
	
	Game joinGame(int gameId, String username);
	
	Game setPlayerReady (String username, Game inputGame);
	
	boolean allPlayersReady (int gameId);
	
	User getUser(User user);

	Question getQuestion(int topicId);

	Question getQuestionFromQuestionId(int questionId);

	Question getRandomQuestion(int topicId);

}
