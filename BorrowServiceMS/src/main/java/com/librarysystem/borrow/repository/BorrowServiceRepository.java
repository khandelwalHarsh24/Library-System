package com.librarysystem.borrow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.librarysystem.borrow.entity.Borrow;

@Repository
public interface BorrowServiceRepository extends CrudRepository<Borrow,Long> {
	@Query(value = "SELECT COUNT(*) FROM borrow_records WHERE user_id = ?1 AND status = 'Borrowed'", nativeQuery = true)
	public Long countByUserIdAndStatusBorrowed(Long userId);
	public Boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId,String status);
	public Optional<Borrow>  findByUserIdAndBookIdAndStatus(Long userId,Long bookId,String status);
	public List<Borrow> findByUserIdAndStatus(Long userId,String status);
	List<Borrow> findByUserId(Long userId);
}
