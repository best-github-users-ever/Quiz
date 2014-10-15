package com.quiz.quizcontroller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.opensymphony.xwork2.ActionContext;
import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.model.User;

@Controller
public class QuizController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6321821026030843446L;
	private static Logger log = Logger.getLogger(QuizController.class);

	@RequestMapping("/")
	public ModelAndView welcomeAction() {

		ModelAndView model = new ModelAndView("login");

		return model;
	}

	@RequestMapping("/request-topics-u.action")
	public ModelAndView goToTopicsAction() {

		ModelAndView model = new ModelAndView("topics-u");

		return model;
	}

	@RequestMapping("/login-again.action")
	public ModelAndView loginAgainAction() {

		ModelAndView model = new ModelAndView("login");

		return model;
	}

	@RequestMapping("/logout-u.action")
	public ModelAndView logoutAction(HttpSession session) {

		session.removeAttribute("user");

		ModelAndView model = new ModelAndView("login");

		return model;
	}

	@RequestMapping(value = "/request-new-account.action")
	public ModelAndView requestNewAccountAction() {

		ModelAndView model = new ModelAndView("newAccount");

		return model;
	}

	@RequestMapping(value = "/new-account.action", method = RequestMethod.POST)
	public ModelAndView newAccountAction(
			@ModelAttribute("user") User user, HttpServletRequest request) {

		ModelAndView model = new ModelAndView("login");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		if (!dao.addUser(user)) {
			request.setAttribute("reqErrorMessage",
					"Username '" + user.getUserId() + "' already exists.");

		} else {
			request.setAttribute("reqPositiveMessage",
					"User " + user.getUserId() + " added! Please login.");
		}
		
		//@ModelAttribute added user to model. remove it so user will login
		model.getModelMap().remove("user");

		return model;
	}

	@RequestMapping(value = "/show-hint.action/{userId}", method = RequestMethod.GET)
	public ModelAndView showHintAction(@PathVariable ("userId") String userId,
			HttpServletRequest request) {

		log.info("in showHintAction.");
		log.info("userid:" + userId);
		ModelAndView model = new ModelAndView();
		model.setViewName("/login");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		String hint = dao.showHint(userId);

		if ("".equals(hint)) {
			request.setAttribute("reqErrorMessage",
					"No hint available for Username of '" + userId + "'. This may not be a valid user.");

		} else {
			request.setAttribute("reqPositiveMessage",
					"Hint: '" + hint + "'");
		}

		return model;
	}

	@RequestMapping(value = "/login.action", method = RequestMethod.POST)
	public ModelAndView loginAction(@ModelAttribute("user") User user,
			HttpServletRequest request, HttpSession session) {

		log.info("in LoginAction::in execute.");
		log.info(user.toString());

		ModelAndView model = new ModelAndView();
		
		//ensure old user removed from session
		session.removeAttribute("user");

		IQuizDbAccess dao = DBAccess.getDbAccess();

		User localUser = dao.getUser(user);

		if (localUser != null) {

			model.setViewName("topics-u");
			session.setAttribute("user", localUser);

		} else {
			model.setViewName("login");
			model.addObject("userName", user.getUserId().trim());

			request.setAttribute("reqErrorMessage",
					"Username Password combination is not correct");
			//@ModelAttribute added user to model. remove it so user will login
			model.getModelMap().remove("user");
		}


		//remove from request to prevent jsp from using it.
		request.removeAttribute("user");

		return model;
	}

	@RequestMapping(value = "/chooseQuizTopic-u.action", method = RequestMethod.POST)
	public ModelAndView chooseQuizTopicAction(
			@RequestParam("topicId") int topicId,
			@RequestParam("numberPlayers") int numberPlayers,
			HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView();

		log.info("in chooseQuizTopicAction");
		log.info("topicid:" + topicId);

		IQuizDbAccess dao = DBAccess.getDbAccess();
		log.info("value of dao is null:" + (dao == null));

		model.addObject("numberPlayers", numberPlayers);

		Game game = null;

		game = dao.findGameForNewPlayer(topicId, numberPlayers,
				((User) session.getAttribute("user")).getUserId());

		if (game != null) {

			model.addObject("game", game);

			if (game.getTotalPlayers() == game.getNumPlayers()) {

				request.setAttribute("reqPositiveMessage",
						"All Players found! Start Quiz Now!");
				request.setAttribute("allPlayersFound", true);
				
				if(game.getTotalPlayers() > 1){
					//message the other players that game is ready
					
				}
				
				
				// need to 'randomly' get a question from the topic.
				Question question = dao.getQuestion(topicId);

				if (question != null) {
					model.setViewName("quiz-u");
					session.setAttribute("question", question);

					return model;
				} else {
					// no questions for topic

					model.setViewName("topics-u");
					request.setAttribute("reqErrorMessage",
							"No Questions available for this topic");
					return model;
				}

			} else {
				model.setViewName("topics-u");
				request.setAttribute("reqPositiveMessage",
						"Game Found! Please wait until other players join.");
				return model;
			}
		} else {
			model.setViewName("topics-u");
			request.setAttribute("reqErrorMessage",
					"Error finding a game for selected Topic and number of players");
			return model;
		}

	}

	@RequestMapping(value = "/answerQuestion-u.action", method = RequestMethod.POST)
	public ModelAndView answerQuestionAction(
			@RequestParam("option") int option, HttpServletRequest request,
			HttpSession session) {

		ModelAndView model = new ModelAndView();
		model.setViewName("quiz-u");

		log.info("in answerQuestionAction");
		log.info("option:" + option);

		Question question = (Question) session.getAttribute("question");

		if (question != null) {

			if (question.getAnswerIdx() == option) {
				request.setAttribute("reqPositiveMessage", "Correct!");

			} else {

				request.setAttribute("reqErrorMessage", "Wrong!");
			}

		} else {
			// no questions for topic

			request.setAttribute("reqErrorMessage", "Question Not Found");
		}

		return model;
	}

}
