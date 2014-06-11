package com.quiz.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport implements SessionAware,
		                                                          ServletRequestAware,
		                                                          RequestAware,
		                                                          ParameterAware, 
		                                                          ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	// Field to store session context.
	private Map<String, Object> session;
	
	// Field to store request content.
	private Map<String, Object> request;
	
	// Field to store request parameters.
	private Map<String, String[]> parameters;
	
	// Field to store HttpServletRequest.
	private HttpServletRequest servletRequest = null;
	
	// Field to store HttpServletResponse.
	private HttpServletResponse servletResponse = null;
	
	// Result code for session expired action.
	public final static String SESSION_EXPIRED = "SessionExpired";
	
	// Result code for Logout action.
	public final static String LOGOUT = "logout";
	
	// action to be triggered when session expires
    @Override
	public String execute() throws Exception {
		return executeAction();
	}

	// override in your actions
	public abstract String executeAction() throws Exception;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
		
	}

	public Map<String, Object> getRequest() {
		return request;
		
	}

	@Override
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public Map<String, String[]> getParameters() {
		return parameters;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}

	public String getCurrentUser() {
		return (String) session.get("currentUser");
	}

	public void setCurrentUser(String currentUser) {
		session.put("currentUser", currentUser);
	}
}