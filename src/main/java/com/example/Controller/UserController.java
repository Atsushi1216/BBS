package com.example.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.Entity.User;
import com.example.Service.UserService;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	// ユーザー登録画面の表示
	@GetMapping("/signup")
	public ModelAndView signup() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/signup");
		return mav;
	}

	// ユーザ登録処理
	@PostMapping("/postSignup")
	public ModelAndView postSignup(@ModelAttribute("formModel") User user) {

		String rawPassword = user.getPassword();

		Hasher hasher = Hashing.sha256().newHasher();
		hasher.putString(rawPassword, Charsets.UTF_8);
		HashCode sha256 = hasher.hash();

		// Hashcode型のsha256をString型に変換
		String strSha256 = String.valueOf(sha256);

		// User entityのPasswordに変換した値をセットする
		user.setPassword(strSha256);

		userService.saveUser(user);
		return new ModelAndView("redirect:/");

	}


}
