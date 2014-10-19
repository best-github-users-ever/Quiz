package com.quiz.socket.controller;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.quiz.quizcontroller.QuizController;
import com.quiz.socket.gamelogic.GameResult;
import com.quiz.socket.gamelogic.JoinInput;
@Controller
public class JoinGameWebSocketController {
    private SimpMessagingTemplate template;  
    
	private static Logger log = Logger.getLogger(JoinGameWebSocketController.class);

	@Autowired
	JoinGameWebSocketController(SimpMessagingTemplate template){
		this.template = template;
	}
	    
	public SimpMessagingTemplate getTemplate(){
		return template;
	}
	
    @MessageMapping("/joinGame" )
//    @SendTo("/topic/gameUpdates")
    public GameResult joinGameMessage(JoinInput input) throws Exception {
        GameResult result = new GameResult("gameFound", "You have successfully joined game: " + input.getGameId()); 
        template.convertAndSend("/queue/"+ input.getGameId()+"/"+input.getUserId() +"/gameUpdates", result);
        return result;
    }
    
    public static void sendGameReadyMessage(SimpMessagingTemplate templateIn, int gameId, String userId, String opponent){
       GameResult result = new GameResult("gameReady", "Game with user '" + opponent + "' can now begin!"); 
       log.info("result:" + result);
       log.info("template is null:" + (templateIn == null));
      
       templateIn.convertAndSend("/queue/"+ gameId +"/"+ userId +"/gameUpdates", result );
    	
    }
} 
