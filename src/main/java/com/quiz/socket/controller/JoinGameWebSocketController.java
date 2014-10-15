package com.quiz.socket.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.quiz.socket.gamelogic.GameResult;
import com.quiz.socket.gamelogic.JoinInput;
@Controller
public class JoinGameWebSocketController {
//    @Autowired private SimpMessagingTemplate template;  

    @MessageMapping("/joinGame" )
    @SendTo("/topic/gameUpdates")
    public GameResult joinGameMessage(JoinInput input) throws Exception {
        GameResult result = new GameResult("You have successfully joined game: " + input.getGameId()); 
        return result;
    }
} 