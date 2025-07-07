package com.librarysystem.borrow.service;

import java.util.List;

import com.librarysystem.borrow.dto.BookDTO;
import com.librarysystem.borrow.dto.BorrowDTO;
import com.librarysystem.borrow.exception.BorrowServiceException;

public interface BorrowService {
	public String borrowBook(Long userId,Long bookId,String token) throws BorrowServiceException;
	public String returnedBook(Long userId,Long bookId,String token) throws BorrowServiceException;
	public List<BorrowDTO> currentBorrowBook(Long userId) throws BorrowServiceException;
	public List<BorrowDTO> borrowHistory(Long userId) throws BorrowServiceException;
}
