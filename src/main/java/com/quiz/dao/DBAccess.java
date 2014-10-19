package com.quiz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private static IQuizDbAccess dbo;

	private static Logger log = Logger.getLogger("com.quiz.dao.DBAccess");

	private JdbcTemplate jdbcTemplate = null;

	// New
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	private DBAccess() {
	}

	public void setNamedJdbcTemplate(
			NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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

		public UserPreparedStatementCreator(User user) {
			this.user = user;
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

	// DB Access methods below

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
	public Question getQuestionFromQuestionId(int questionId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE questionid = ?";

		try {
			return (Question) jdbcTemplate.queryForObject(GET_QUESTION, new Object[] {
					questionId}, new QuestionMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public Question getQuestion(int topicId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";
		int questionIndex = 0;

		List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
				new Object[] { topicId}, new QuestionMapper());

		if (!candidateQuestions.isEmpty()) {
			//just return the first question
			return (Question) candidateQuestions.get(questionIndex);
		} else {
			return null;
		}

	}

	@Override
	public Question getRandomQuestion(int topicId) {

		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";

		List<Object> candidateQuestions = jdbcTemplate.query(GET_QUESTION,
				new Object[] { topicId}, new QuestionMapper());

		if (!candidateQuestions.isEmpty()) {

			int questionIndex = (int) Math.round(  Math.random() * (candidateQuestions.size()-1)  );
			
			return (Question) candidateQuestions.get(questionIndex);
		} else {
			return null;
		}

	}
	
	private static class GamePreparedStatementCreator implements
			PreparedStatementCreator {
		private String GAME_INSERT = "INSERT INTO games (topicid, totplayers) VALUES(?,?)";
		private Game game;

		@Override
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			PreparedStatement ps = conn.prepareStatement(GAME_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, game.getTopicId());
			ps.setInt(2, game.getTotalPlayers());
			return ps;
		}

		public GamePreparedStatementCreator(Game game, int topicId,
				int totalPlayers) {
			this.game = game;
			this.game.setTopicId(topicId);
			this.game.setTotalPlayers(totalPlayers);
		}
	}

	private static final class GameMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			Game localGame = new Game();
			localGame.setGameId(rs.getInt("gameId"));
			localGame.setTopicId(rs.getInt("topicid"));
			localGame.setNumPlayers(rs.getInt("numplayers"));
			localGame.setTotalPlayers(rs.getInt("totplayers"));
			localGame.setPlayer1(rs.getString("player1"));
			localGame.setPlayer2(rs.getString("player2"));
			localGame.setPlayer3(rs.getString("player3"));
			localGame.setPlayer4(rs.getString("player4"));
			localGame.setPlayer5(rs.getString("player5"));
			// TODO Auto-generated method stub
			return localGame;
		}
	}

	public Game retrieveGamefromId(int gameId) {
		final String GET_GAME = "SELECT * FROM games WHERE gameid = ?";
		return (Game) jdbcTemplate.queryForObject(GET_GAME,
				new Object[] { gameId }, new GameMapper());

	}

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
	public String showHint(String userId) {
		final String GET_HINT = "SELECT * FROM users WHERE userid = ?";
		User user = null;
		
		try {
	    	user = (User) jdbcTemplate.queryForObject(GET_HINT,
				new Object[] { userId }, new UserMapper());
		    } 
		catch (EmptyResultDataAccessException e) {
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
