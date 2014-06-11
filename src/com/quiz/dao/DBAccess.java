package com.quiz.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			dbo = (IQuizDbAccess) ctx.getBean("employeeJdbcImplDao");
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
	public void addUser(User user) {
		UserPreparedStatementCreator creator = new UserPreparedStatementCreator(
				user);
		// KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(creator);
	}

	@Override
	public User getUser(User user) {
//		log.error(user.toString());
		//need to include password below
		final String GET_USER = "SELECT * FROM users WHERE userid = ? AND password = ?";
		try{
		return (User) jdbcTemplate.queryForObject(GET_USER,
				new Object[] { user.getUserId(), user.getPassword() }, new UserMapper());
		}
		catch (EmptyResultDataAccessException e){
			return null;
		}
	}


	@Override
	public Question getQuestion(int topicId) {
		final String GET_QUESTION = "SELECT * FROM questions WHERE topicid = ?";
		return (Question) jdbcTemplate.queryForObject(GET_QUESTION,
				new Object[] { topicId }, new QuestionMapper());
	}

}
