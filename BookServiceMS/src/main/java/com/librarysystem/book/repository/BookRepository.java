package com.librarysystem.book.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.librarysystem.book.entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book,Long>, PagingAndSortingRepository<Book,Long> {
	public Book findByIsbn(String isbn);
	public List<Book> findByTitle(String title, Pageable pageable);
    public List<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}
