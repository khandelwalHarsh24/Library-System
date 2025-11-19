package com.librarysystem.auth.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.librarysystem.auth.exception.AuthException;

@Component
@Aspect
public class LoggingAspect {

	public static final Logger LOGGER = 
			LogManager.getLogger(LoggingAspect.class); 
	
	@AfterThrowing(pointcut = "execution(* com.librarysystem.auth.service.*.*(..))", throwing = "exception")
	public void afterThrowing(AuthException exception) throws AuthException {
		LOGGER.error(exception.getMessage(), exception);
	}
}
