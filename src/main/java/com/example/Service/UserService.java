package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.User;
import com.example.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	// user情報を全件取得
	public List<User> findAllUser() {
		return userRepository.findAll();
	}

	// ユーザー登録のためレコード追加メソッド
	public void saveUser(User user) {
		userRepository.save(user);
	}

	public User getLoginUser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}


}
