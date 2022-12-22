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

	// ログインのためemailとpasswordを取得
	public User getLoginUser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	// ユーザ編集のため1件取得
	public User findUser(Integer id) {
		User user = userRepository.findById(id).orElse(null);
		return user;
	}

	// ユーザ編集のためにUserのアカウント名を取得
	public User findUserAccount(String account) {
		return userRepository.findByAccount(account);
	}

	public User findUserName(String name) {
		return userRepository.findByName(name);
	}

	public User findUserEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
