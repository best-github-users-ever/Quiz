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
		        +       "NAME       VARCHAR(30)  UNIQUE NOT NULL,"
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
				+       "create Table IF NOT EXISTS GAMES ("
				+       "GAMEID  MEDIUMINT  AUTO_INCREMENT,"
				+       "TOPICID   MEDIUMINT NOT NULL,"
		        +       "TOTPLAYERS SMALLINT NOT NULL,"
		        +       "NUMPLAYERS SMALLINT DEFAULT 0,"
		        +       "CURR_Q_INDEX SMALLINT DEFAULT 0,"
		        +       "Q1ID SMALLINT DEFAULT 0,"
		        +       "Q2ID SMALLINT DEFAULT 0,"
		        +       "Q3ID SMALLINT DEFAULT 0,"
		        +       "Q4ID SMALLINT DEFAULT 0,"
		        +       "Q5ID SMALLINT DEFAULT 0,"
		        +       "Q6ID SMALLINT DEFAULT 0,"
		        +       "Q7ID SMALLINT DEFAULT 0,"
		        +       "Q8ID SMALLINT DEFAULT 0,"
		        +       "Q9ID SMALLINT DEFAULT 0,"
		        +       "Q10ID SMALLINT DEFAULT 0,"
		        +       "PLAYER1   VARBINARY(40)  DEFAULT NULL,"
		        +       "P1_READY  BOOL           DEFAULT FALSE,"
		        +       "P1_NUM_CORRECT SMALLINT  DEFAULT 0,"
		        +       "P1_NUM_WRONG   SMALLINT  DEFAULT 0,"
		        +       "P1_NUM_NO_ANS  SMALLINT  DEFAULT 0,"
		        +       "P1_TIME   BIGINT         DEFAULT 0,"
		        +       "P1_CURR_Q_DONE BOOL      DEFAULT FALSE,"
		        +       "PLAYER2   VARBINARY(40)  DEFAULT NULL,"
		        +       "P2_READY  BOOL           DEFAULT FALSE,"
		        +       "P2_NUM_CORRECT SMALLINT  DEFAULT 0,"
		        +       "P2_NUM_WRONG   SMALLINT  DEFAULT 0,"
		        +       "P2_NUM_NO_ANS  SMALLINT  DEFAULT 0,"
		        +       "P2_TIME   BIGINT         DEFAULT 0,"
		        +       "P2_CURR_Q_DONE BOOL      DEFAULT FALSE,"
		        +       "PLAYER3   VARBINARY(40)  DEFAULT NULL,"
		        +       "P3_READY  BOOL           DEFAULT FALSE,"
		        +       "P3_NUM_CORRECT SMALLINT  DEFAULT 0,"
		        +       "P3_NUM_WRONG   SMALLINT  DEFAULT 0,"
		        +       "P3_NUM_NO_ANS  SMALLINT  DEFAULT 0,"
		        +       "P3_TIME   BIGINT         DEFAULT 0,"
		        +       "P3_CURR_Q_DONE BOOL      DEFAULT FALSE,"
		        +       "PLAYER4   VARBINARY(40)  DEFAULT NULL,"
		        +       "P4_READY  BOOL           DEFAULT FALSE,"
		        +       "P4_NUM_CORRECT SMALLINT  DEFAULT 0,"
		        +       "P4_NUM_WRONG   SMALLINT  DEFAULT 0,"
		        +       "P4_NUM_NO_ANS  SMALLINT  DEFAULT 0,"
		        +       "P4_TIME   BIGINT         DEFAULT 0,"
		        +       "P4_CURR_Q_DONE BOOL      DEFAULT FALSE,"
		        +       "PLAYER5   VARBINARY(40)  DEFAULT NULL,"
		        +       "P5_READY  BOOL           DEFAULT FALSE,"
		        +       "P5_NUM_CORRECT SMALLINT  DEFAULT 0,"
		        +       "P5_NUM_WRONG   SMALLINT  DEFAULT 0,"
		        +       "P5_NUM_NO_ANS  SMALLINT  DEFAULT 0,"
		        +       "P5_TIME   BIGINT         DEFAULT 0,"
		        +       "P5_CURR_Q_DONE BOOL      DEFAULT FALSE,"
		        +       "KEY GAMEID (GAMEID) "
                +       ") ENGINE=InnoDB;" + "  COMMIT;");

		writer.println("use QUIZDB;"
				+       "ALTER TABLE GAMES ADD FOREIGN KEY (TOPICID) REFERENCES TOPICS (TOPICID)"
				+       "ON UPDATE CASCADE "
                +       "ON DELETE RESTRICT; COMMIT;");	

		writer.println("use QUIZDB;"
				+       "create Table IF NOT EXISTS QUESTIONS ("
				+       "QUESTIONID     MEDIUMINT  AUTO_INCREMENT,"
				+       "TOPICID   MEDIUMINT DEFAULT NULL,"
		        +       "QUESTION  VARBINARY(200)  UNIQUE NOT NULL,"
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
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'What kind of animal is Binky Barnes ?', 'Who knows?', 'Hippo', 'Lemur', 'Bulldog', 3);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'Which two characters in the Arthur gang share the same middle name ?', 'Arthur and Binkey', 'Francine and Muffy', 'Carlos and Wanda', 'Buster and Brain', 1);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'What is the real name of Binkey ?', 'Shelley', 'Bernard', 'Bincard', 'Binthrope', 0);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'Where is Ladonna from ?', 'Mexico', 'Louisiana', 'Florida', 'Utah', 1);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'Who the birth mother of Pal ?', 'Ms. Woods', 'Princess', 'Pancreas', 'Perky', 3);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'Who is the current piano teacher of Arthur ?', 'Dr. Fugue', 'Mrs. Cardigan ', 'Mr. Caninus', 'Senora Perez', 0);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'What song did Dr. Fugue teach to the class of Mr. Ratburn ?', 'Tea for Two', 'Hello Dolly', 'In the Good Old Summertime', 'Make Lemonade from These Lemons!', 2);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'Pal is worried she may not be top dog when this dog makes a visit ?', 'Sandy', 'Amigo', 'Hocum', 'Treenie', 1);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'What is the full name of The Brain ?', 'Alan Powers', 'Reginald Smith', 'Marcus Welby', 'Fred Sanford', 0);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 3, 'In what city does Artur take place ?', 'Lakewood City', 'Elwood City', 'Reed City', 'Sherman', 1);");
	    writer.println(" INSERT INTO QUESTIONS VALUES ('DEFAULT', 4, 'Which horror movie ended with a burning house ?', 'The Amityville Horror', 'Carrie', 'all of them', 'The Shining', 2);");

	}
}

