package com.quiz.socket.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quiz.socket.demologic.CalcInput;
import com.quiz.socket.demologic.Result;
@Controller
public class WebSocketController {
    @MessageMapping("/add" )
    @SendTo("/topic/showResult")
    public Result addNum(CalcInput input) throws Exception {
        Thread.sleep(2000);
        Result result = new Result(input.getNum1()+"+"+input.getNum2()+"="+(input.getNum1()+input.getNum2())); 
        return result;
    }
    @RequestMapping("/start")
    public String start() {
    	System.out.println("returning start from controller");
        return "start";
    }
} 