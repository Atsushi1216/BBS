package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Message;
import com.example.Repository.MessageRepository;

@Service
public class MessageService {

	@Autowired
	MessageRepository messageRepository;

	public List<Message> findAllMessage(){
		return messageRepository.findAll();
	}

	public void saveMessage(Message message) {
		messageRepository.save(message);
	}

	public void deleteMessage(Integer id) {
		messageRepository.deleteById(id);
	}
}
