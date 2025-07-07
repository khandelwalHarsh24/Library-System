package com.librarysystem.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.book.dto.BookDTO;
import com.librarysystem.book.exception.BookServiceException;
import com.librarysystem.book.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
public class BookServiceController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("/add")
	public ResponseEntity<String> addBooks(@RequestBody BookDTO book) throws BookServiceException{
		String isbn=bookService.addBooks(book);
		return new ResponseEntity<String>("Books Added with Isbn number "+isbn,HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateBooks(@PathVariable Long id,@RequestBody BookDTO book) throws BookServiceException{
		String isbn=bookService.updateBooks(id, book);
		return new ResponseEntity<String>("Books Updated with Isbn number "+isbn,HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable Long id) throws BookServiceException{
		bookService.deleteBooks(id);
		return new ResponseEntity<String>("Book deleted with Isbn number ",HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<BookDTO>> getBooks(@RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws BookServiceException{
		List<BookDTO> books=bookService.getAllBooks(title, author, page, size);
		return new ResponseEntity<List<BookDTO>>(books,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBook(@PathVariable Long id) throws BookServiceException{
		BookDTO book=bookService.getBook(id);
		return new ResponseEntity<BookDTO>(book,HttpStatus.OK);
	}
	
	@PutMapping("decrement/{id}")
	public ResponseEntity<String> decrementAvailableCopies(@PathVariable Long id) throws BookServiceException{
		String msg=bookService.decrementCopies(id);
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	@PutMapping("increment/{id}")
	public ResponseEntity<String> incrementAvailableCopies(@PathVariable Long id) throws BookServiceException{
		String msg=bookService.incrementCopies(id);
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	
	
	
}
