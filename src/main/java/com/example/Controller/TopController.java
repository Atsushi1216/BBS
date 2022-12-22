package com.example.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.Entity.Message;
import com.example.Entity.User;
import com.example.Service.MessageService;

@Controller
public class TopController {

	@Autowired
	MessageService messageService;

	@Autowired
	HttpSession session;

	@Autowired
	LoginController loginController;

	// TOP画面表示
	@GetMapping("/")
	public ModelAndView top() {
		ModelAndView mav = new ModelAndView();

		User user = (User) session.getAttribute("loginUser");

		// user情報がなければログイン画面へ遷移
		if (user == null) {
			mav.setViewName("/login");
			return mav;
		}

		// 投稿を全件取得
		List<Message> messageText = messageService.findAllMessage();

		mav.addObject("message", messageText);
		mav.addObject("loginUser", user);
		mav.setViewName("/top");
		return mav;
	}

	// つぶやき投稿処理
	@PostMapping("/postMessage")
	public ModelAndView postMessage(@RequestParam String text) {
		// 投稿内容をセットするため、インスタンスを生成
		ModelAndView mav = new ModelAndView();
		Message message = new Message();
		List<String> errorMessages = new ArrayList<String>();
		String strMessage = String.valueOf(message);

		User user = (User) session.getAttribute("loginUser");

		if (user == null) {
			mav.setViewName("/login");
			return mav;
		}

		if (!(Strings.isBlank(strMessage))) {
			errorMessages.add("文字を記入せずに投稿はできません");
		} else if(140 < strMessage.length()) {
			errorMessages.add("141文字以上は投稿できません");
		}

		message.setUserId(user.getId());
		message.setUser(user);
		message.setText(text);

		//エラーメッセージ数をチェック
		if (errorMessages.size() != 0) {
			mav.setViewName("redirect:/");
			mav.addObject("errorMessages", errorMessages);
			return mav;
		}

		messageService.saveMessage(message);

		return new ModelAndView("redirect:/");
	}

	// つぶやき削除処理
	@DeleteMapping("/delete/{id}")
	public ModelAndView deleteText(@PathVariable Integer id) {
		messageService.deleteMessage(id);
		return new ModelAndView("redirect:/");
	}
}
