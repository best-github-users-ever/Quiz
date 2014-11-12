package com.quiz.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class HttpSessionCollector implements HttpSessionListener {
    private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
	private static Logger log = Logger.getLogger(HttpSessionCollector.class);


	@Override
	public void sessionCreated(HttpSessionEvent event) {
		log.info("**********");
	        HttpSession session = event.getSession();
	        sessions.put(session.getId(), session);
			log.info("session MAP:" + sessions.toString());
	
	}


	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
		
	}

    public static HttpSession find(String sessionId) {
        return sessions.get(sessionId);
    }
    
    


}