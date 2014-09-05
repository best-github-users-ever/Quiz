package com.quiz.dao;

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

public interface IQuizDbAccess {
	
	void addUser(User u);
	
	Game findGameForNewPlayer(int topicId, int totalPlayers, String username);
	
	Game addGame(int topicId, int totalPlayers);
	
	void updateGame(Game game);

	Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers);

	Game retrieveGamefromId(int gameId);
	
	Game joinGame(int gameId, String username);

	User getUser(User user);

	Question getQuestion(int topicId);

}
