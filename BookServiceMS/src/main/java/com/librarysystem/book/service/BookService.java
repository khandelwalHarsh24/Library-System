package com.librarysystem.book.service;

import java.util.List;

import org.hibernate.query.Page;

import com.librarysystem.book.dto.BookDTO;
import com.librarysystem.book.exception.BookServiceException;

public interface BookService {
	public String addBooks(BookDTO book) throws BookServiceException;
	public String updateBooks(Long id,BookDTO book) throws BookServiceException;
	public void deleteBooks(Long id) throws BookServiceException;
	public List<BookDTO> getAllBooks(String title,String author,int page,int size) throws BookServiceException;
	public BookDTO getBook(Long id) throws BookServiceException;
	public BookDTO searchBook() throws BookServiceException;
	public String decrementCopies(Long id)throws BookServiceException;
	public String incrementCopies(Long id)throws BookServiceException;
}
