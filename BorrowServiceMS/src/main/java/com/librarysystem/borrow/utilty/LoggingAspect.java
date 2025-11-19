package com.librarysystem.borrow.utilty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.librarysystem.borrow.exception.BorrowServiceException;

@Component
@Aspect
public class LoggingAspect {

	public static final Logger LOGGER = 
			LogManager.getLogger(LoggingAspect.class); 
	
	@AfterThrowing(pointcut = "execution(* com.librarysystem.borrow.service.*.*(..))", throwing = "exception")
	public void afterThrowing(BorrowServiceException exception) throws BorrowServiceException {
		LOGGER.error(exception.getMessage(), exception);
	}
}
