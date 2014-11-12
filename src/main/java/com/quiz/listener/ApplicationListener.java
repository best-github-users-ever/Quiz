package com.quiz.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.quiz.dao.DBAccess;
import com.quiz.dao.IQuizDbAccess;


public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

			ServletContext sc   = sce.getServletContext();

			IQuizDbAccess dao = DBAccess.getDbAccess();

	        sc.setAttribute("topicList", dao.getTopics());
	        
	        //below is the full path of the user images directory
//	        String quizPath = sc.getRealPath("/");
//	        sc.setAttribute("userImagesPath", quizPath + "resources\\images\\");	        
		
	}

}
