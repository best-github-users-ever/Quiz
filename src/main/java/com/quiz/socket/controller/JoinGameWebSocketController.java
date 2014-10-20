package com.quiz.socket.controller;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;
import com.quiz.model.Game;
import com.quiz.model.Question;
import com.quiz.socket.gamelogic.AnswerInput;
import com.quiz.socket.gamelogic.GameQuestionResult;
import com.quiz.socket.gamelogic.GameResult;
import com.quiz.socket.gamelogic.JoinInput;

@Controller
public class JoinGameWebSocketController  {
    private SimpMessagingTemplate template;  
    private Map<String, Object> sessionMap;
    
	private static Logger log = Logger.getLogger(JoinGameWebSocketController.class);

	@Autowired
	JoinGameWebSocketController(SimpMessagingTemplate template){
		this.template = template;
	}
	    
	public SimpMessagingTemplate getTemplate(){
		return template;
	}
	
    @MessageMapping("/joinGame/ready" )
    public void readyGameMessage(JoinInput input) throws Exception {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = null;
		
		String userId = input.getUserId();
		Game game = dao.retrieveGamefromId(input.getGameId());

		game = dao.setPlayerReady(userId, game);

		//need to deal with game = null error here
		
		if (dao.allPlayersReady(game.getGameId())) {

			GameResult delayMessage = new GameResult("delayBeforeStart", "5");
	        template.convertAndSend("/topic/"+ input.getGameId() + "/gameUpdates", delayMessage );

	        try {
	        	//sleep an extra second to allow for propagation delay
	            Thread.sleep(6000);      
	        } catch(InterruptedException ex) {
	            Thread.currentThread().interrupt();
	        }
			
			question = dao.getRandomQuestion(game.getTopicId());
	        GameQuestionResult result = new GameQuestionResult("question", 1, 10, question); 
	        log.info("result:" + result.toString());
	       
	        template.convertAndSend("/topic/"+ input.getGameId() + "/gameUpdates", result );
		} else {
		       GameResult result = new GameResult("continueWaiting", "Waiting for other player to be ready in order to begin!"); 
		       log.info("result:" + result);
		      
		       template.convertAndSend("/queue/"+ input.getGameId() +"/"+ userId +"/gameUpdates", result );

		}
    
    }
    public static void sendGameReadyMessage(SimpMessagingTemplate templateIn, int gameId, String userId, String opponent){
       GameResult result = new GameResult("gameReady", "Game with user '" + opponent + "' can now begin!"); 
       log.info("result:" + result);
       log.info("template is null:" + (templateIn == null));
      
       templateIn.convertAndSend("/queue/"+ gameId +"/"+ userId +"/gameUpdates", result );
    	
    }
    
    @MessageMapping("/joinGame/answer" )
    public void answerQuestionMessage(AnswerInput input) throws Exception {
		IQuizDbAccess dao = DBAccess.getDbAccess();

		Question question = dao.getQuestionFromQuestionId(input.getQuestionId());

		if (question != null) {

			if (question.getAnswerIdx() == input.getGuess()) {
			       GameResult result = new GameResult("answerRight", "Correct!"); 
			       log.info("result:" + result);
			      
			       template.convertAndSend("/queue/"+ input.getGameId() +"/"+ input.getUserId() +"/gameUpdates", result );


			} else {

			       GameResult result = new GameResult("answerWrong", "Wrong!"); 
			       log.info("result:" + result);
			      
			       template.convertAndSend("/queue/"+ input.getGameId() +"/"+ input.getUserId() +"/gameUpdates", result );
			}

		} else {
			// no questions for topic

		       GameResult result = new GameResult("answerResult", "Error! QuestionId" + input.getQuestionId() + " was not found!"); 
		       log.info("result:" + result);
		      
		       template.convertAndSend("/queue/"+ input.getGameId() +"/"+ input.getUserId() +"/gameUpdates", result );
		}

    }

} 
