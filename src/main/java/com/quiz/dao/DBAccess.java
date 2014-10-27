package com.quiz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DBAccess implements IQuizDbAccess {
	private static final class GameMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			Game localGame = new Game();
			localGame.setGameId(rs.getInt("gameId"));
			localGame.setTopicId(rs.getInt("topicid"));
			localGame.setNumPlayers(rs.getInt("numplayers"));
			localGame.setTotalPlayers(rs.getInt("totplayers"));
			localGame.setCurrQIndex(rs.getInt("curr_q_index"));
			localGame.setPlayer1(rs.getString("player1"));
			localGame.setP1Ready(rs.getBoolean("p1_ready"));
			localGame.setP1NumCorrect(rs.getInt("p1_num_correct"));
			localGame.setP1Time(rs.getDouble("p1_time"));
			localGame.setP1CurrQDone(rs.getBoolean("p1_curr_q_done"));
			localGame.setPlayer2(rs.getString("player2"));
			localGame.setP2Ready(rs.getBoolean("p2_ready"));
			localGame.setP2NumCorrect(rs.getInt("p2_num_correct"));
			localGame.setP2Time(rs.getDouble("p2_time"));
			localGame.setP2CurrQDone(rs.getBoolean("p2_curr_q_done"));
			localGame.setPlayer3(rs.getString("player3"));
			localGame.setP3Ready(rs.getBoolean("p3_ready"));
			localGame.setP3NumCorrect(rs.getInt("p3_num_correct"));
			localGame.setP3Time(rs.getDouble("p3_time"));
			localGame.setP3CurrQDone(rs.getBoolean("p3_curr_q_done"));
			localGame.setPlayer4(rs.getString("player4"));
			localGame.setP4Ready(rs.getBoolean("p4_ready"));
			localGame.setP4NumCorrect(rs.getInt("p4_num_correct"));
			localGame.setP4Time(rs.getDouble("p4_time"));
			localGame.setP4CurrQDone(rs.getBoolean("p4_curr_q_done"));
			localGame.setPlayer5(rs.getString("player5"));
			localGame.setP5Ready(rs.getBoolean("p5_ready"));
			localGame.setP5NumCorrect(rs.getInt("p5_num_correct"));
			localGame.setP5Time(rs.getDouble("p5_time"));
			localGame.setP5CurrQDone(rs.getBoolean("p5_curr_q_done"));
			// TODO Auto-generated method stub
			return localGame;
		}
	}

	private static class GamePreparedStatementCreator implements
			PreparedStatementCreator {
		private String GAME_INSERT = "INSERT INTO games (topicid, totplayers) VALUES(?,?)";
		private Game game;

		public GamePreparedStatementCreator(Game game, int topicId,
				int totalPlayers) {
			this.game = game;
			this.game.setTopicId(topicId);
			this.game.setTotalPlayers(totalPlayers);
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(GAME_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, game.getTopicId());
			ps.setInt(2, game.getTotalPlayers());
			return ps;
		}
	}

	private static final class QuestionMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			Question localQuestion = new Question();

			localQuestion.setQuestionId(rs.getInt("questionid"));
			localQuestion.setTopicId(rs.getInt("topicid"));
			localQuestion.setQuestion(rs.getString("question"));
			localQuestion.setOption1(rs.getString("option1"));
			localQuestion.setOption2(rs.getString("option2"));
			localQuestion.setOption3(rs.getString("option3"));
			localQuestion.setOption4(rs.getString("option4"));
			localQuestion.setAnswerIdx(rs.getInt("answeridx"));

			return localQuestion;
		}
	}

	private static final class UserMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			User localUser = new User();
			localUser.setUserId(rs.getString("userid"));
			localUser.setEmailAddress(rs.getString("email"));
			localUser.setPassword(rs.getString("password"));
			localUser.setHint(rs.getString("hint"));
			// TODO Auto-generated method stub
			return localUser;
		}
	}

	private static class UserPreparedStatementCreator implements
			PreparedStatementCreator {
		private String USER_INSERT = "INSERT INTO users (userid, hint, email, password) VALUES(?,?,?,?)";
		private User user;

		public UserPreparedStatementCreator(User user) {
			this.user = user;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(USER_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUserId());
			ps.setString(2, user.getHint());
			ps.setString(3, user.getEmailAddress());
			ps.setString(4, user.getPassword());
			return ps;
		}
	}

	public static IQuizDbAccess getDbAccess() {
		if (dbo == null) {
			dbo = new DBAccess();
			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					"context.xml");
			dbo = (IQuizDbAccess) ctx.getBean("quizJdbcImplDao");
			return dbo;
		} else {
			return dbo;
		}
	}

	private static IQuizDbAccess dbo;

	private static Logger log = Logger.getLogger("com.quiz.dao.DBAccess");

	private JdbcTemplate jdbcTemplate = null;

	// New
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	private DBAccess() {
	}

	// DB Access methods below

	@Override
	public Game addGame(int topicId, int totalPlayers) {
		Game game = new Game();
		GamePreparedStatementCreator creator = new GamePreparedStatementCreator(
				game, topicId, totalPlayers);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(creator, keyHolder);
		if (keyHolder.getKey() != null) {
			return retrieveGamefromId(keyHolder.getKey().intValue());
		} else {
			return null;

		}

	}

	@Override
	public boolean addUser(User user) {
		UserPreparedStatementCreator creator = new UserPreparedStatementCreator(
				user);
		// KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(creator);
			return true;

		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean allPlayersFinishedQuestion(int gameId){

		Game game = retrieveGamefromId(gameId);

		if (game != null) {

			if (game.getTotalPlayers() == 2) {
				return game.isP1CurrQDone() & game.isP2CurrQDone();
			} else if (game.getTotalPlayers() == 3) {
				return game.isP1CurrQDone() & game.isP2CurrQDone() & game.isP3CurrQDone();
			} else if (game.getTotalPlayers() == 4) {
				return game.isP1CurrQDone() & game.isP2CurrQDone() & game.isP3CurrQDone()
						& game.isP4CurrQDone();
			} else if (game.getTotalPlayers() == 5) {
				return game.isP1CurrQDone() & game.isP2CurrQDone() & game.isP3CurrQDone()
						& game.isP4CurrQDone() & game.isP5CurrQDone();
			} else if (game.getTotalPlayers() == 1) {
				return game.isP1CurrQDone();
			} else {
				log.info("value of total players is invalid in gameId: "
						+ gameId);
				return false;
			}

		} else {
			log.info("couldn't find game based on gameId of:" + gameId);
			return false;
		}
	}
	
	@Override
	public boolean allPlayersReady(int gameId) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {

			if (game.getTotalPlayers() == 2) {
				return (game.isP1Ready() & game.isP2Ready());
			} else if (game.getTotalPlayers() == 3) {
				return game.isP1Ready() & game.isP2Ready() & game.isP3Ready();
			} else if (game.getTotalPlayers() == 4) {
				return game.isP1Ready() & game.isP2Ready() & game.isP3Ready()
						& game.isP4Ready();
			} else if (game.getTotalPlayers() == 5) {
				return game.isP1Ready() & game.isP2Ready() & game.isP3Ready()
						& game.isP4Ready() & game.isP5Ready();
			} else if (game.getTotalPlayers() == 1) {
				return game.isP1Ready();
			} else {
				log.info("value of total players is invalid in gameId: "
						+ gameId);
				return false;
			}

		} else {
			log.info("couldn't find game based on gameId of:" + gameId);
			return false;
		}
	}

	@Override
	public Game findGameForNewPlayer(int topicId, int totalPlayers,
			String username) {

		Game game = searchForFirstMatchingQueuedGame(topicId, totalPlayers);

		if (game != null) {
			game = joinGame(game.getGameId(), username);

			log.info("joined existing game");
			return game;
		} else {
			game = addGame(topicId, totalPlayers);

			if (game != null) {
				game = joinGame(game.getGameId(), username);

				log.info("added new game");

				return game;
			}

		}

		return null;
	}

	@Override
	public List<String> getOtherPlayerUserIds(int gameId, String userId) {

		Game game = retrieveGamefromId(gameId);
		List <String> opponentList = new ArrayList<String>();

		log.info(game.toString());
 		if ((game.getPlayer1() != null) & (game.getPlayer1() != "") & (!userId.equals(game.getPlayer1()))){
 			opponentList.add(game.getPlayer1());
		} 
 		if ((game.getPlayer2() != null) & (game.getPlayer2() != "") & (!userId.equals(game.getPlayer2()))){
 			opponentList.add(game.getPlayer2());
		} 
 		if ((game.getPlayer3() != null) & (game.getPlayer3() != "") & (!userId.equals(game.getPlayer3()))){
 			opponentList.add(game.getPlayer3());
		} 
 		if ((game.getPlayer4() != null) & (game.getPlayer4() != "") & (!userId.equals(game.getPlayer4()))){
 			opponentList.add(game.getPlayer4());
		} 
 		if ((game.getPlayer5() != null) & (game.getPlayer5() != "") & (!userId.equals(game.getPlayer5()))){
 			opponentList.add(game.getPlayer5());
		} 
		return opponentList;
	}

	@Override
	public Question getQuestion(int topicId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";
		int questionIndex = 0;

		List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
				new Object[] { topicId }, new QuestionMapper());

		if (!candidateQuestions.isEmpty()) {
			// just return the first question
			return (Question) candidateQuestions.get(questionIndex);
		} else {
			return null;
		}

	}

	@Override
	public Question getQuestionFromQuestionId(int questionId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE questionid = ?";

		try {
			return (Question) jdbcTemplate.queryForObject(GET_QUESTION,
					new Object[] { questionId }, new QuestionMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public Question getRandomQuestion(int topicId) {

		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";

		List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
				new Object[] { topicId }, new QuestionMapper());

		if (!candidateQuestions.isEmpty()) {

			int questionIndex = (int) Math.round(Math.random()
					* (candidateQuestions.size() - 1));

			return (Question) candidateQuestions.get(questionIndex);
		} else {
			return null;
		}

	}

	@Override
	public User getUser(User user) {
		// log.error(user.toString());
		// need to include password below
		final String GET_USER = "SELECT * FROM users WHERE userid = ? AND password = ?";
		try {
			return (User) jdbcTemplate.queryForObject(GET_USER, new Object[] {
					user.getUserId(), user.getPassword() }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public int incrementQuestionNumber(int gameId){
		Game game = retrieveGamefromId(gameId);
		
		if (game != null) {
			int newIndex = game.getCurrQIndex() + 1;

			String updateStatement = " UPDATE games" + 
			" SET CURR_Q_INDEX = ? WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { newIndex, gameId });

			return newIndex;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return -1;

		}
	}
	
	@Override
	public Game joinGame(int gameId, String username) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			if (game.getNumPlayers() < game.getTotalPlayers()) {
				String playerString = null;
				String playerUserId = null;

				int playerNumber = game.getNumPlayers() + 1;

				playerUserId = username;

				switch (playerNumber) {
				case 1:
					game.setPlayer1(username);
					playerString = "player1";
					break;
				case 2:
					game.setPlayer2(username);
					playerString = "player2";
					break;
				case 3:
					game.setPlayer3(username);
					playerString = "player3";
					break;
				case 4:
					game.setPlayer4(username);
					playerString = "player4";
					break;
				case 5:
					game.setPlayer5(username);
					playerString = "player5";
					break;

				default:
					log.severe("Incorrect player number: " + playerNumber);
					break;
				}

				game.setNumPlayers(game.getNumPlayers() + 1);
				log.info(game.toString());

				String updateStatement = " UPDATE games"
						+ " SET numplayers= ?," + playerString + " = ?"
						+ " WHERE gameid= ?";

				jdbcTemplate.update(
						updateStatement,
						new Object[] { game.getNumPlayers(), playerUserId,
								game.getGameId() });

			}
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);

		}
		return game;

	}

	@Override
	public Game resetPlayersQDone(Game inputGame) {
		int gameId = inputGame.getGameId();
		Game game = retrieveGamefromId(gameId);

		if (game != null) {

			String updateStatement = " UPDATE games" + 
			" SET P1_CURR_Q_DONE = false, " +
			" SET P2_CURR_Q_DONE = false, " +
			" SET P3_CURR_Q_DONE = false, " +
			" SET P4_CURR_Q_DONE = false, " +
			" SET P5_CURR_Q_DONE = false  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { gameId });

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}

	}

	public Game retrieveGamefromId(int gameId) {
		final String GET_GAME = "SELECT * FROM games WHERE gameid = ?";
		return (Game) jdbcTemplate.queryForObject(GET_GAME,
				new Object[] { gameId }, new GameMapper());

	}

	@Override
	public Game searchForFirstMatchingQueuedGame(int topicId, int totalPlayers) {
		final String GET_GAME = "SELECT * FROM games WHERE topicid = ? AND totplayers = ? AND numplayers < totplayers";

		List<Object> candidateGames = jdbcTemplate.query(GET_GAME,
				new Object[] { topicId, totalPlayers }, new GameMapper());

		if (!candidateGames.isEmpty()) {
			return (Game) candidateGames.get(0);
		} else {
			return null;
		}

	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setNamedJdbcTemplate(
			NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	@Override
	public Game setPlayerReady(String username, int gameId) {
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			String playerReadyString = null;

			if (username.equals(game.getPlayer1())) {
				game.setP1Ready(true);
				playerReadyString = "p1_ready";
			} else if (username.equals(game.getPlayer2())) {
				game.setP2Ready(true);
				playerReadyString = "p2_ready";
			} else if (username.equals(game.getPlayer3())) {
				game.setP3Ready(true);
				playerReadyString = "p3_ready";
			} else if (username.equals(game.getPlayer4())) {
				game.setP4Ready(true);
				playerReadyString = "p4_ready";
			} else if (username.equals(game.getPlayer5())) {
				game.setP5Ready(true);
				playerReadyString = "p5_ready";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

			String updateStatement = " UPDATE games" + " SET "
					+ playerReadyString + " = true" + " WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { gameId });

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Game setPlayerCorrectAnswer(String username, int gameId){
		Game game = retrieveGamefromId(gameId);
		int newCorrectCount = 0;

		if (game != null) {
			String playerFinishedQuestionString = null;
			String playerCorrectAnswerString = null;

			if (username.equals(game.getPlayer1())) {
				game.setP1CurrQDone(true);
				newCorrectCount = game.getP1NumCorrect() + 1;
				game.setP1NumCorrect(newCorrectCount);
				playerFinishedQuestionString = "p1_curr_q_done";
				playerCorrectAnswerString = "P1_NUM_CORRECT";
			} else if (username.equals(game.getPlayer2())) {
				newCorrectCount = game.getP2NumCorrect() + 1;
				game.setP2NumCorrect(newCorrectCount);
				game.setP2CurrQDone(true);
				playerFinishedQuestionString = "p2_curr_q_done";
				playerCorrectAnswerString = "P2_NUM_CORRECT";
			} else if (username.equals(game.getPlayer3())) {
				newCorrectCount = game.getP3NumCorrect() + 1;
				game.setP3NumCorrect(newCorrectCount);
				game.setP3CurrQDone(true);
				playerFinishedQuestionString = "p3_curr_q_done";
				playerCorrectAnswerString = "P3_NUM_CORRECT";
			} else if (username.equals(game.getPlayer4())) {
				newCorrectCount = game.getP4NumCorrect() + 1;
				game.setP4NumCorrect(newCorrectCount);
				game.setP4CurrQDone(true);
				playerFinishedQuestionString = "p4_curr_q_done";
				playerCorrectAnswerString = "P4_NUM_CORRECT";
			} else if (username.equals(game.getPlayer5())) {
				newCorrectCount = game.getP5NumCorrect() + 1;
				game.setP5NumCorrect(newCorrectCount);
				game.setP5CurrQDone(true);
				playerFinishedQuestionString = "p5_curr_q_done";
				playerCorrectAnswerString = "P5_NUM_CORRECT";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true, " 
		         	+ playerCorrectAnswerString + " = ?  WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { newCorrectCount, gameId });

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}

	@Override
	public Game setPlayerFinishedQuestion(String username, int gameId){
		Game game = retrieveGamefromId(gameId);

		if (game != null) {
			String playerFinishedQuestionString = null;

			if (username.equals(game.getPlayer1())) {
				game.setP1CurrQDone(true);
				playerFinishedQuestionString = "p1_curr_q_done";
			} else if (username.equals(game.getPlayer2())) {
				game.setP2CurrQDone(true);
				playerFinishedQuestionString = "p2_curr_q_done";
			} else if (username.equals(game.getPlayer3())) {
				game.setP3CurrQDone(true);
				playerFinishedQuestionString = "p3_curr_q_done";
			} else if (username.equals(game.getPlayer4())) {
				game.setP4CurrQDone(true);
				playerFinishedQuestionString = "p4_curr_q_done";
			} else if (username.equals(game.getPlayer5())) {
				game.setP5CurrQDone(true);
				playerFinishedQuestionString = "p5_curr_q_done";
			}

			else {
				log.severe("Incorrect username: " + username);
				return null;
			}

			String updateStatement = " UPDATE games" + " SET "
					+ playerFinishedQuestionString + " = true WHERE gameId = ?";

			jdbcTemplate.update(updateStatement, new Object[] { gameId });

			return game;
		} else {
			log.info("couldn't find game based on gameid of:" + gameId);
			return null;

		}
	}
	
	@Override
	public String showHint(String userId) {
		final String GET_HINT = "SELECT * FROM users WHERE userid = ?";
		User user = null;

		try {
			user = (User) jdbcTemplate.queryForObject(GET_HINT,
					new Object[] { userId }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return "";
		}

		if (user != null) {

			if (user.getHint() == null) {
				return "";
			} else {
				return user.getHint();
			}
		} else {
			return null;
		}
	}

}
