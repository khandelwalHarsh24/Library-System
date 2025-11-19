package com.librarysystem.user.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.librarysystem.user.exception.UserServiceException;

@Component
@Aspect
public class LoggingAspect {

	
	public static final Logger LOGGER = 
			LogManager.getLogger(LoggingAspect.class); 
	
	@AfterThrowing(pointcut = "execution(* com.librarysystem.user.service.*.*(..))", throwing = "exception")
	public void afterThrowing(UserServiceException exception) throws UserServiceException {
		LOGGER.error(exception.getMessage(), exception);
	}
}
