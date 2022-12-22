package com.example.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.Entity.User;
import com.example.Service.UserService;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	// ログイン画面の表示
	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		User user = new User();

		mav.addObject("user", user);
		mav.setViewName("/login");
		return mav;
	}

	// ログイン処理
	@PostMapping("/postLogin")
	public ModelAndView postLogin(@ModelAttribute("loginUser") User user,
			@RequestParam(name="email") String email,
			@RequestParam(name="password") String password) {
//		ModelAndView mav = new ModelAndView();

		String rawPassword = user.getPassword();

		Hasher hasher = Hashing.sha256().newHasher();
		hasher.putString(rawPassword, Charsets.UTF_8);
		HashCode sha256 = hasher.hash();

		// Hashcode型のsha256をString型に変換
		String strSha256 = String.valueOf(sha256);

		// User entityのPasswordに変換した値をセットする
		user.setPassword(strSha256);

		// Serviceとメソッド名を同一にし、検索
		user = userService.getLoginUser(user.getEmail(), strSha256);

		// ユーザ情報をセッション領域へセット
		session.setAttribute("loginUser", user);

		return new ModelAndView("redirect:/");
	}

	// ログアウト処理
	@GetMapping("/logout")
	public ModelAndView logout() {
		// セッションを無効化
		session.invalidate();
		return new ModelAndView("redirect:/login");
	}
}
