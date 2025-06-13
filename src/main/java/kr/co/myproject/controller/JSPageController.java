package kr.co.myproject.controller;


import kr.co.myproject.service.NoticeService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class JSPageController {
	@Autowired
	private NoticeService noticeService;
    
    @GetMapping("/login-page")
	public String loginPage()
	{
		return "/login.html";
	}

	@GetMapping("/notice-check-page")
	public String noticeCheckPage(@RequestParam int idx) {
		int queryCount = noticeService.plusNoticeViewCount(idx);

		if(queryCount == 0)
		{
			String msg = URLEncoder.encode("글 정보를 불러올 수 없습니다", StandardCharsets.UTF_8);
        	return "index.html?result=" + msg;
		}
		
		return "noticeCheck.html?idx=" + idx;
	}

	@GetMapping("/notice-modify-page")
	public String noticeModifyPage(@RequestParam int idx) {
		return "noticeModify.html?idx=" + idx;
	}

	@GetMapping("/notice-add-page")
	public String noticeAddPage() {
		return "noticeAdd.html";
	}
	
}
