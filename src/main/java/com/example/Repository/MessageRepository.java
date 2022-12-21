package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Message;

@Repository
public interface MessageRepository extends JpaRepository <Message, Integer>{

}
