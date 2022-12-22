package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.User;

@Repository
public interface UserRepository extends JpaRepository <User, Integer>{
	User findByEmailAndPassword(String email, String password);
	User findByAccount(String account);
	User findByName(String name);
	User findByEmail(String email);
}
