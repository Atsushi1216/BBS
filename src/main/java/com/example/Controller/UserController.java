package com.example.Controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@Autowired
	HttpSession session;

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

	// ユーザ編集画面
	@GetMapping("/setting/{id}")
	public ModelAndView setting() {
		ModelAndView mav = new ModelAndView();
		User user = (User) session.getAttribute("loginUser");
		mav.addObject("loginUser", user);
		mav.setViewName("/setting");
		return mav;
	}

	// ユーザ編集処理
	@PutMapping("/postSetting/{id}")
	public ModelAndView postSetting(@PathVariable Integer id, @ModelAttribute("loginUser") User user, User existAccount, User existName) {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User) session.getAttribute("loginUser");
		List<String> errorMessages = new ArrayList<String>();

		// フォームに入力されたアカウント名
		String account = user.getAccount();
		// DBに登録されてるアカウント名か確認
		existAccount = userService.findUserAccount(account);
		// 編集前のセッション領域にあるアカウント名
		String sameAccount = loginUser.getAccount();

		//バリデーション(アカウント名)
		if (Strings.isBlank(account)) {
			errorMessages.add("アカウント名を入力してください");
		}else if (10 < account.length()) {
			errorMessages.add("アカウント名は10文字以下で入力してください");
		} else if (existAccount != null && !account.equals(sameAccount)) {
			errorMessages.add("既に使用されているアカウント名です");
		}

		// 以下、nameも同様
		String name = user.getName();
		existName = userService.findUserName(name);

		//バリデーション(ユーザー名)
		if (Strings.isBlank(name)) {
			errorMessages.add("ユーザー名を入力してください");
		}else if (10 < name.length()) {
			errorMessages.add("ユーザー名は10文字以下で入力してください");
		}

		// 直接入力されるパスワードの値をnullにする
		String rawPassword = null;
		rawPassword = user.getPassword();

		if (Strings.isBlank(rawPassword)) {
			errorMessages.add("パスワードを入力してください");
		} else {
			Hasher hasher = Hashing.sha256().newHasher();
			hasher.putString(rawPassword, Charsets.UTF_8);
			HashCode sha256 = hasher.hash();

			// Hashcode型のsha256をString型に変換
			String strSha256 = String.valueOf(sha256);

			// User entityのPasswordに変換した値をセットする
			user.setPassword(strSha256);
		}

			// 以下記述でupdateカラムにcurrenttimeを追加
			// Timestamp型変数の初期値を設定
			Timestamp updatedDate = null;
			// フォーマットを変数に指定
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// Date型の変数を指定し、指定したフォーマットを変換
			Date date = new Date();
			String strDate = sdf.format(date);

			// Timestamp型へString型で受け取ったdateを変換
			updatedDate = Timestamp.valueOf(strDate);
			// 編集する情報をセット
			mav.addObject("loginUser", loginUser);

			// UrlParameterのidとupdateDateを更新するentityにセット
			user.setId(id);
			user.setUpdatedDate(updatedDate);

		//エラーメッセージを数をチェック
		if (errorMessages.size() != 0) {
			mav.addObject("errorMessages", errorMessages);
			mav.setViewName("/setting");
			return mav;
		}

		userService.saveUser(user);

		// user情報をsession領域に"loginUser"として保存する
		session.setAttribute("loginUser", user);
		return new ModelAndView("redirect:/");
	}
}
