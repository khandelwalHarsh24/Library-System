package com.librarysystem.book.service;


import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.librarysystem.book.dto.BookDTO;
import com.librarysystem.book.entity.Book;
import com.librarysystem.book.exception.BookServiceException;
import com.librarysystem.book.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public String addBooks(BookDTO book) throws BookServiceException {
		Book bookInfo=bookRepo.findByIsbn(book.getIsbn());
		if(bookInfo!=null) {
			throw new BookServiceException("Book Already Exist for this ISBN");
		}
		if(book.getCreated_at()==null) {
			book.setCreated_at(LocalDateTime.now());
		}
		return bookRepo.save(modelMapper.map(book,Book.class)).getIsbn();
	}

	@Override
	public String updateBooks(Long id,BookDTO book) throws BookServiceException {
		Book bookInfo=bookRepo.findById(id).orElseThrow(()->new BookServiceException("Book Does not Exist with this Id"));
		System.out.println(bookInfo);
		bookInfo.setTitle(book.getTitle());
		bookInfo.setAuthor(book.getAuthor());
		bookInfo.setGenre(book.getGenre());
		bookInfo.setIsbn(book.getIsbn());
		bookInfo.setTotalCopies(book.getTotalCopies());
		bookInfo.setAvailableCopies(book.getAvailableCopies());
		// TODO Auto-generated method stub
		return bookRepo.save(bookInfo).getIsbn();
	}

	@Override
	public void deleteBooks(Long id) throws BookServiceException {
		// TODO Auto-generated method stub
		Book book = bookRepo.findById(id)
	            .orElseThrow(() -> new BookServiceException("Book not found with ID: " + id));
		bookRepo.delete(book);
	}

	@Override
	public List<BookDTO> getAllBooks(String title,String author,int page,int size) throws BookServiceException {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
		if (title != null && !title.isBlank()) {
            List<Book> books=bookRepo.findByTitle(title, pageable);
            return modelMapper.map(books, new TypeToken<List<BookDTO>>() {
    		}.getType());
        }
		if(author!=null && !author.isBlank()) {
			List<Book> books=bookRepo.findByAuthorContainingIgnoreCase(author, pageable);
			return modelMapper.map(books, new TypeToken<List<BookDTO>>() {
    		}.getType());
		}
		List<Book>books=bookRepo.findAll(pageable).getContent();
		return modelMapper.map(books, new TypeToken<List<BookDTO>>() {
		}.getType());
	}

	@Override
	public BookDTO getBook(Long id) throws BookServiceException {
		// TODO Auto-generated method stub
		Book book=bookRepo.findById(id).orElseThrow(()->new BookServiceException("Book not found with ID: " + id));
		return modelMapper.map(book, BookDTO.class);
	}

	@Override
	public BookDTO searchBook() throws BookServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decrementCopies(Long id) throws BookServiceException {
		// TODO Auto-generated method stub
		Book bookInfo=bookRepo.findById(id).orElseThrow(()->new BookServiceException("Book not found with ID: " + id));
		bookInfo.setAvailableCopies(bookInfo.getAvailableCopies()-1);
		bookRepo.save(bookInfo);
		return "Available Copies Updated";
	}

	@Override
	public String incrementCopies(Long id) throws BookServiceException {
		// TODO Auto-generated method stub
		Book bookInfo=bookRepo.findById(id).orElseThrow(()->new BookServiceException("Book not found with ID: " + id));
		if(bookInfo.getAvailableCopies()==bookInfo.getTotalCopies()) throw new BookServiceException("Available Copies equal to total copies");
		bookInfo.setAvailableCopies(bookInfo.getAvailableCopies()+1);
		bookRepo.save(bookInfo);
		return "Available Copies Updated";
	}
	
}
