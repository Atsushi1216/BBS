package com.example.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
		Message message = new Message();

		User user = (User) session.getAttribute("loginUser");

		message.setUserId(user.getId());
		message.setUser(user);
		message.setText(text);

		messageService.saveMessage(message);

		return new ModelAndView("redirect:/");
	}
}
