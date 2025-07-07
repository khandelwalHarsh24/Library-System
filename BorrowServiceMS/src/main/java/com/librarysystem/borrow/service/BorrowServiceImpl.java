package com.librarysystem.borrow.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.librarysystem.borrow.dto.BookDTO;
import com.librarysystem.borrow.dto.BorrowDTO;
import com.librarysystem.borrow.entity.Borrow;
import com.librarysystem.borrow.exception.BorrowServiceException;
import com.librarysystem.borrow.repository.BorrowServiceRepository;
import com.librarysystem.common.dto.NotifyRequest;

@Service("borrowService")
public class BorrowServiceImpl implements BorrowService  {
	
	@Autowired
	BorrowServiceRepository borrowRepo;
	
	@Autowired
    private KafkaProducerService kafkaProducerService;
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public String borrowBook(Long userId,Long bookId,String token) throws BorrowServiceException{
		Long count=borrowRepo.countByUserIdAndStatusBorrowed(userId);
		if(count>=3) {
			throw new BorrowServiceException("Limit exceeded: Max 3 books allowed.");
		}
		Boolean alreadyBorrowed=borrowRepo.existsByUserIdAndBookIdAndStatus(userId, bookId,"Borrowed");
		if(alreadyBorrowed) {
			throw new BorrowServiceException("Already Borrowed this book");
		}
		BookDTO book=webClient.get().uri("http://localhost:4000/api/v1/books/"+bookId).header("Authorization", token).retrieve().bodyToMono(BookDTO.class).block();
		if(book.getAvailableCopies()<=0) {
			throw new BorrowServiceException("Books not available");
		}
		String msg=webClient.put().uri("http://localhost:4000/api/v1/books/decrement/"+bookId).header("Authorization", token).retrieve().bodyToMono(String.class).block();
		Borrow record = new Borrow();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(14)); // US-21
        record.setStatus("Borrowed");
        NotifyRequest event = new NotifyRequest(
                "khandelwalharsh0003@gmail.com",
                "Book Borrowed",
                "You borrowed '" + book.getTitle() + "' and it’s due on " + record.getDueDate()
            );
        kafkaProducerService.sendNotification(event);
        borrowRepo.save(record);
        return msg;
	}

	@Override
	public String returnedBook(Long userId,Long bookId,String token) throws BorrowServiceException {
		// TODO Auto-generated method stub
		Borrow record = borrowRepo.findByUserIdAndBookIdAndStatus(userId, bookId,"Borrowed")
                .orElseThrow(() -> new BorrowServiceException("No active borrow found."));
		record.setStatus("Returned");
		record.setReturnDate(LocalDateTime.now());
		borrowRepo.save(record);
	
		String msg=webClient.put().uri("http://localhost:4000/api/v1/books/increment/"+bookId).header("Authorization", token).retrieve().bodyToMono(String.class).block();      
		return msg;
	}

	@Override
	public List<BorrowDTO> currentBorrowBook(Long userId) throws BorrowServiceException {
		// TODO Auto-generated method stub
		List<Borrow> borrowBook=borrowRepo.findByUserIdAndStatus(userId, "Borrowed");
		if(borrowBook==null) throw new BorrowServiceException("No Borrowed Book");
		return modelMapper.map(borrowBook,new TypeToken<List<BorrowDTO>>() {
		}.getType());
	}
	
	

	@Override
	public List<BorrowDTO> borrowHistory(Long userId) throws BorrowServiceException{
		// TODO Auto-generated method stub
		List<Borrow> borrowHistory=borrowRepo.findByUserId(userId);
		if(borrowHistory==null || borrowHistory.size()==0) throw new BorrowServiceException("No Borrowed Book");
		return modelMapper.map(borrowHistory,new TypeToken<List<BorrowDTO>>() {
		}.getType());
	}
	
	
	
}
