package com.quiz.dbgenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GenerateDB {
	
	public static void main(String[] args) throws ParseException,
	FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("db.sql",
				"UTF-8");

		GenerateDB.generateTabCreateCmds(writer);
		GenerateDB.generateTopics(writer);
		GenerateDB.generateQuestions(writer);


		writer.close();

}

	public static void generateTabCreateCmds(PrintWriter writer) {
		writer.println("create database QUIZDB;" 
                + "use QUIZDB;"
	            + "create Table IF NOT EXISTS TOPICS ("
		        +       "TOPICID MEDIUMINT AUTO_INCREMENT,"
		        +       "NAME       VARCHAR(30)  NOT NULL,"
		        +       "PRIMARY KEY (TOPICID)"
		        +       ") ENGINE=InnoDB;" + "  COMMIT;");
		
		writer.println("use QUIZDB;"
	    	            + "create Table IF NOT EXISTS USERS ("
	    		        +       "USERID   VARBINARY(40)  UNIQUE NOT NULL,"
	    		        +       "PASSWORD VARBINARY(40)  NOT NULL,"
	    		        +       "HINT     VARBINARY(80)  NOT NULL,"
	    		        +       "EMAIL    VARCHAR(80)  NULL,"
	    		        +       "PRIMARY KEY (USERID)"
	    		        +       ") ENGINE=InnoDB;" + "  COMMIT;");
	
		writer.println("use QUIZDB;"
				+       "create Table IF NOT EXISTS QUESTIONS ("
				+       "QUESTIONID     MEDIUMINT  AUTO_INCREMENT,"
				+       "TOPICID   MEDIUMINT DEFAULT NULL,"
		        +       "QUESTION  VARBINARY(200)  NOT NULL,"
		        +       "OPTION1   VARBINARY(200)  NOT NULL,"
		        +       "OPTION2   VARBINARY(200)  NOT NULL,"
		        +       "OPTION3   VARBINARY(200)  NOT NULL,"
		        +       "OPTION4   VARBINARY(200)  NOT NULL,"
		        +       "ANSWERIDX SMALLINT NOT NULL,"
		        +       "KEY QUESTIONID (QUESTIONID) "
                +       ") ENGINE=InnoDB;" + "  COMMIT;");
		
		writer.println("use QUIZDB;"
				+       "ALTER TABLE QUESTIONS ADD FOREIGN KEY (TOPICID) REFERENCES TOPICS (TOPICID)"
				+       "ON UPDATE CASCADE "
                +       "ON DELETE RESTRICT; COMMIT;");	
                 
	}
	
	public static void generateTopics(PrintWriter writer) {
		writer.println(" INSERT INTO TOPICS VALUES ('DEFAULT', 'Sports');");
		writer.println(" INSERT INTO TOPICS VALUES ('DEFAULT', 'Monster Movies');");
		writer.println(" INSERT INTO TOPICS VALUES ('DEFAULT', 'Arthur');");
		writer.println(" INSERT INTO TOPICS VALUES ('DEFAULT', 'Horror Movies');");
	}
	
	public static void generateQuestions(PrintWriter writer) {
		writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 1, 'What sport was played by Arthur Ashe ?', 'Football', 'Baseball', 'Tennis', 'Auto Racing', 2);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 2, 'Name the actor who played Godzilla originally ?', 'Buddy Zilla', 'Raymond Burr', 'it was a model', 'James Dean', 2);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'What kind of animal is Binky Barnes ?', 'Who knows?', 'Aardvark', 'Lemur', 'Hippo', 3);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 4, 'Which horror movie ended with a burning house ?', 'The Amityville Horror', 'Carrie', 'all of them', 'The Shining', 2);");

	}
}

