package kr.co.myproject.controller;

import kr.co.myproject.Util.Util;
import kr.co.myproject.service.NoticeService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class JSPageController {
	@Autowired
	private NoticeService noticeService;

	private final Logger logger = LoggerFactory.getLogger(JSPageController.class);

    
    @GetMapping("/login-page")
	public String loginPage()
	{
		return "/login.html";
	}

	@GetMapping("/notice-check-page")
	public String noticeCheckPage(@RequestParam int idx,
								   HttpServletRequest httpServletRequest) {
		int queryCount = noticeService.plusNoticeViewCount(idx);

		if(queryCount == 0)
		{
			String msg = URLEncoder.encode("글 정보를 불러올 수 없습니다", StandardCharsets.UTF_8);
        	return "index.html?result=" + msg;
		}
		Util.SaveFinalURL(httpServletRequest);
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

	@GetMapping("/board-list-page")
	public String boardListPage() {
		return "boardList.html";
	}
	
	@GetMapping("/board-add-page")
	public String boardAddPage() {
		return "boardAdd.html";
	}
	
	@GetMapping("/board-check-page")
	public String boardCheckPage(@RequestParam int idx) {
		return "boardCheck.html?idx=" + idx;
	}

	@GetMapping("/board-modify-page")
	public String getMethodName(@RequestParam int idx) {
		return "boardModify.html?idx=" + idx;
	}
	
	@GetMapping("/board-report-page")
	public String reportPage(@RequestParam int idx) {
		return "boardReport.html?=idx" + idx;
	}

	@GetMapping("/resume-board-check-page")
    public String ResumeBoardCheckPage() {
        return "resumeBoardCheck.html";
    }

	@GetMapping("/projects-board-list-page")
	public String projectList() {
		return "ProjectsBoardList.html";
	}
	
	@GetMapping("/projects-check-page")
	public String projectCheck(@RequestParam int idx) {
		return "ProjectsBoardCheck.html?=idx" + idx;
	}
	


}
