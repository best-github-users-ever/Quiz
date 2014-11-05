package com.quiz.dao;

import java.util.List;

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

public interface IQuizDbAccess {

	Game addGame(int topicId, int totalPlayers);

	boolean addUser(User u);

	boolean allPlayersFinishedQuestion(int gameId);

	boolean allPlayersReady(int gameId);

	Game findGameForNewPlayer(int topicId, int totalPlayers, String username);

	List<String> getAllPlayerUserIds(int gameId);

	List<String> getOtherPlayerUserIds(int gameId, String userId);

	Question getQuestion(int topicId);

	Question getQuestionFromQuestionId(int questionId);
	
	Question getRandomQuestion(int topicId);

	User getUser(User user);
	
	int incrementQuestionNumber(int gameId);

	Game joinGame(int gameId, String username);

	Game resetPlayersQDone(Game inputGame);

	Game retrieveGamefromId(int gameId);

	Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers);
	
	Game setPlayerCorrectAnswer(String username, double answerTime, int gameId);
	
	Game setPlayerNoAnswer(String username, int gameId);
	
	Game setPlayerReady(String username, int gameId);

	Game setPlayerWrongAnswer(String username, int gameId);
	
	Game setPlayerFinishedQuestion(String username, int gameId);
	
	Game setRemainingPlayersNoAnswer(int gameId);
	
	String showHint(String userId);

}
