package com.quiz.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.quiz.socket.controller.JoinGameWebSocketController;

public class Utility {


	private static Logger log = Logger
			.getLogger(Utility.class);

	public static boolean validAlphabetic(String string) {
		return (Pattern.matches("[a-zA-Z]+", string) | string.matches(" ") | string
				.matches("-"));
	}

	public static boolean validNumeric(String string) {
		return (Pattern.matches("[0-9]+", string));
	}

	public static String getFooterDate(){
		SimpleDateFormat java_date_format = new SimpleDateFormat(
				"EEE MMMM dd HH:mm:ss z yyyy");
		Date now = new java.util.Date();

		java.util.Date date;

		try {
			date = (java.util.Date) java_date_format.parse(now.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		// create SimpleDateFormat object with desired date format
		SimpleDateFormat web_format1 = new SimpleDateFormat("EEE MMMM dd yyyy");

		SimpleDateFormat web_format2 = new SimpleDateFormat("HH:mm z");
		
		return (web_format1.format(date) + " at " + web_format2.format(date));
	}
	
	public static int determinePoints(double answerTime){
		double seconds = answerTime/1000.0;
		log.info("*** incoming time:" + answerTime + " answer:" + Math.round ( (int) (100*(Math.exp(-0.3 * seconds)))));
		return Math.round ( (int) (100*(Math.exp(-0.3 * seconds))));
		
	}
}
