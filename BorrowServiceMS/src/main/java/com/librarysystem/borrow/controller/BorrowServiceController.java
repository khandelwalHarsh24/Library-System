package com.librarysystem.borrow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.borrow.dto.BorrowDTO;
import com.librarysystem.borrow.exception.BorrowServiceException;
import com.librarysystem.borrow.service.BorrowService;

@RestController
@RequestMapping("/api/v1/borrow")
public class BorrowServiceController {
	
	@Autowired
	BorrowService borrowService;
	
	@PostMapping("book/{userId}")
	public ResponseEntity<String> borrowBook(@PathVariable Long userId,@RequestParam Long bookId,@RequestHeader("Authorization") String token) throws BorrowServiceException{
		String response=borrowService.borrowBook(userId, bookId, token);
		System.out.println(response);
		return new ResponseEntity<String> (response,HttpStatus.OK);
	}
	
	@PostMapping("/return/{userId}")
	public ResponseEntity<String> returnBook(@PathVariable Long userId,@RequestParam Long bookId,@RequestHeader("Authorization") String token) throws BorrowServiceException{
		String response=borrowService.returnedBook(userId, bookId, token);
		return new ResponseEntity<String> (response,HttpStatus.OK);
	}
	
	@GetMapping("/current/{userId}")
	public ResponseEntity<List<BorrowDTO>> currentBorrowBook(@PathVariable Long userId) throws BorrowServiceException{
		List<BorrowDTO> borrowed=borrowService.currentBorrowBook(userId);
		return new ResponseEntity<List<BorrowDTO>> (borrowed,HttpStatus.OK);
	}
	
	@GetMapping("/history/{userId}")
	public ResponseEntity<List<BorrowDTO>> borrowedHistory(@PathVariable Long userId) throws BorrowServiceException{
		List<BorrowDTO> borrowed=borrowService.borrowHistory(userId);
		return new ResponseEntity<List<BorrowDTO>> (borrowed,HttpStatus.OK);
	}
}
