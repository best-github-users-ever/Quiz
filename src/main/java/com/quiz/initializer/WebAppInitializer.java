package com.quiz.initializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.quiz.config.AppWebConfig;
public class WebAppInitializer implements WebApplicationInitializer {
	public void onStartup(ServletContext servletContext) throws ServletException {  
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();  
        ctx.register(AppWebConfig.class);  
        ctx.setServletContext(servletContext); 
        ctx.scan("com.quiz");

        Dynamic dynamic = servletContext.addServlet("spring-dispatcher", new DispatcherServlet(ctx)); 
        dynamic.setAsyncSupported(true);
        dynamic.addMapping("/");  
        dynamic.setLoadOnStartup(1);  

//        context.addListener(new ContextLoaderListener(root));
   }  
}  