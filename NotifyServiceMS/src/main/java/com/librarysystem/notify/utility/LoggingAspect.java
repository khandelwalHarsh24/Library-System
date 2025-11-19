package com.librarysystem.notify.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.librarysystem.notify.exception.NotifyServiceException;

@Component
@Aspect
public class LoggingAspect {

	public static final Logger LOGGER = 
			LogManager.getLogger(LoggingAspect.class); 
	
	@AfterThrowing(pointcut = "execution(* com.librarysystem.notify.service.*.*(..))", throwing = "exception")
	public void afterThrowing(NotifyServiceException exception) throws NotifyServiceException {
		LOGGER.error(exception.getMessage(), exception);
	}
}
