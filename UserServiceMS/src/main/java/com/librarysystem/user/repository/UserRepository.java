package com.librarysystem.user.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.librarysystem.user.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
	public User findByUserNameAndEmail(String name,String email);
	public User findByEmail(String email);
	
}
