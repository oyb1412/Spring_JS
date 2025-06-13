package kr.co.myproject.controller;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.myproject.entity.Board;
import kr.co.myproject.entity.BoardType;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.CommentService;
import kr.co.myproject.service.UserService;
import kr.co.myproject.service.BoardReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;


	@Autowired 
	private BoardReportService boardReportService;

	private final Logger logger = LoggerFactory.getLogger(PageController.class);

	
	// @GetMapping("/board-list-page")
	// public String boardList(@RequestParam(defaultValue = "1") int page,
	// 						@RequestParam(required = false) String searchType,
	// 						@RequestParam(required = false) String keyword,
	// 						@RequestParam(required = false) String sortField,
	// 						@RequestParam(required = false) String sortOrder,
	// 						Model model)
	// {
	// 	int pageSize = 10;
	// 	List<Board> boardList = new ArrayList<>();
	// 	int totalCount = 0;

	// 	if (keyword == null || keyword.trim().isEmpty()) {
    // 		keyword = null; 
    // 		searchType = null; 
	// 	}
		
	// 	//1.검색만 있는 경우
	// 	if ((searchType != null && keyword != null && !keyword.trim().isEmpty())
	// 	&& (sortField == null || sortField.trim().isEmpty() || "default".equals(sortField))) {
	// 		boardList = boardService.searchBoardListPaged(searchType, keyword);
	// 	}
	// 	//2.정렬만 있는 경우
	// 	if((searchType == null || keyword == null || keyword.trim().isEmpty())
	// 	&& sortField != null && !sortField.trim().isEmpty() && !"default".equals(sortField))
	// 	{
	// 		switch (sortField) {
	// 			case "view":
	// 				boardList = boardService.getBoardListDescViewCount();
	// 				break;
	// 			case "comment":
	// 				boardList = boardService.getBoardListDescCommentCount();
	// 				break;
	// 			case "date":
	// 				boardList = boardService.getBoardListIndate();
	// 				break;
	// 		}
	// 		if("desc".equals(sortOrder))
	// 		{
	// 			Collections.reverse(boardList);
	// 		}
	// 	}

	// 	//3.검색, 정렬 모두 해야하는 경우
	// 	if((searchType != null && keyword != null && !keyword.trim().isEmpty()) 
	// 	&& sortField != null && !sortField.trim().isEmpty() && !"default".equals(sortField))
	// 	{
	// 		switch (sortField) {
	// 			case "view":
	// 				boardList = boardService.searchBoardListPagedDescViewCount(searchType, keyword);
	// 				break;
	// 			case "comment":
	// 				boardList = boardService.searchBoardListPagedDescCommentCount(searchType, keyword);
	// 				break;
	// 			case "date":
	// 				boardList = boardService.searchBoardListPagedDescIndate(searchType, keyword);
	// 				break;
	// 		}
	// 		if("desc".equals(sortOrder))
	// 		{
	// 			Collections.reverse(boardList);
	// 		}

	// 	}

	// 	//4.검색, 정렬 아무것도 하지 않는 경우
	// 	if((searchType == null || keyword == null || keyword.trim().isEmpty()) 
	// 	&& (sortField == null || sortField.trim().isEmpty() || "default".equals(sortField)))
	// 	{
	// 		boardList = boardService.getList();
	// 	}
			
	// 	totalCount = boardList.size();
	// 	int startIndex = (page - 1) * pageSize;
	// 	int endIndex = Math.min(startIndex + pageSize, boardList.size());
	// 	boardList = boardList.subList(startIndex, endIndex);

    // 	int totalPage = (int) Math.ceil((double) totalCount / pageSize);
	// 	logger.info("totalPage : "+ totalPage);
    // 	model.addAttribute("boardList", boardList);
    // 	model.addAttribute("currentBoardPage", page);
    // 	model.addAttribute("totalBoardPage", totalPage);
    // 	model.addAttribute("searchType", searchType);
    // 	model.addAttribute("keyword", keyword);

    // 	return "boardList/index";
	// }

	// @GetMapping("/")
	// public String noticeList(@RequestParam(defaultValue = "1") int page, 
	// 						@RequestParam(required = false) String searchType, 
	// 						@RequestParam(required = false) String keyword,
	// 						Model model)
	// {
	// 	int pageSize = 10;
	// 	int start = (page -1) * pageSize;
	// 	List<Notice> noticeList;
	// 	int totalCount;

	// 	if (keyword == null || keyword.trim().isEmpty()) {
    // 		keyword = null; 
    // 		searchType = null; 
	// 	}

    // 	if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
	// 		noticeList = noticeService.searchNoticeListPaged(searchType, keyword, start, pageSize);
	// 		totalCount = noticeService.countNoticeListByType(searchType, keyword); 
	// 	} else {
	// 		noticeList = noticeService.getPagedList(start, pageSize);
	// 		totalCount = noticeService.getList().size();
	// 	}

    // 	int totalPage = (int) Math.ceil((double) totalCount / pageSize);


	// 	model.addAttribute("noticeList", noticeList);
	// 	model.addAttribute("currentNoticePage", page);
	// 	model.addAttribute("totalNoticePage", totalPage);
	// 	model.addAttribute("searchType", searchType);
    // 	model.addAttribute("keyword", keyword);

	// 	return "noticeList/index";
	// }
	
	// @GetMapping("/login-page")
	// public String loginPage()
	// {
	// 	return "login/index";
	// }
	
	@GetMapping("/register-page")
	public String registerPage()
	{
		return "register/index";
	}

	@GetMapping("/dummy")
	public String getMethodName() {
		return "dummy/index";
	}
	
	
	// @GetMapping("/board-add-page")
	// public String boardAddPage(Model model, Authentication authentication, RedirectAttributes redirectAttributes)
	// {
	// 	if(authentication == null || !authentication.isAuthenticated())
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "먼저 로그인을 진행해주세요");
    //     	return "redirect:/board-list-page";
	// 	}

	// 	String writer = userService.findWriter(authentication.getName());

	// 	if(writer == null)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "유저 정보를 불러올 수 없습니다");
    //     	return "redirect:/board-list-page";
	// 	}

	// 	model.addAttribute("writer", writer);
	// 	LocalDateTime now = LocalDateTime.now();
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 	String formattedNow = now.format(formatter);
	// 	model.addAttribute("today", formattedNow);
	// 	return "boardAdd/index";
	// }	
	
	// @GetMapping("/board-check-page")
	// public String boardCheckPage(@RequestParam("idx") int idx, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
	// 	Board board = boardService.findBoard(idx);
	// 	model.addAttribute("board", board);
	// 	int queryCount = boardService.plusBoardViewCount(idx);
	// 	if(queryCount == 0)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "글 정보를 불러올 수 없습니다");
    //     	return "redirect:/board-list-page";
	// 	}

	// 	List<Comment> comments = commentService.findComment(idx, BoardType.FREE);

	// 	if(authentication != null)
	// 	{
	// 		String username = authentication.getName();
    //     	User user = userService.findByUsername(username); 
    //     	model.addAttribute("writer", user.getWriter());
	// 	}
		
	// 	model.addAttribute("comments", comments);
	// 	return "boardCheck/index";
	// }
	
	@GetMapping("/board-modify-page")
	public String boardModifyPage(@RequestParam("idx") int idx,Model model, RedirectAttributes redirectAttributes) {
		Board board = boardService.findBoard(idx);

		if(board == null)
		{
			redirectAttributes.addFlashAttribute("result", "글 정보를 불러올 수 없습니다");
        	return "redirect:/board-list-page";
		}

		model.addAttribute("board", board);
		
		return "boardModify/index";
	}

	// @GetMapping("/notice-add-page")
	// public String noticeAddPage(Model model, Authentication authentication, RedirectAttributes redirectAttributes)
	// {
	// 	String writer = userService.findWriter(authentication.getName());

	// 	if(writer == null)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "유저 정보를 불러올 수 없습니다");
    //     	return "redirect:/";
	// 	}

	// 	model.addAttribute("writer", writer);
	// 	LocalDateTime now = LocalDateTime.now();
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 	String formattedNow = now.format(formatter);
	// 	model.addAttribute("today", formattedNow);
	// 	return "noticeAdd/index";
	// }	
	
	// @GetMapping("/notice-check-page")
	// public String noticeCheckPage(@RequestParam("idx") int idx, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
	// 	Notice notice = noticeService.findNotice(idx);
	// 	model.addAttribute("notice", notice);
	// 	int queryCount = noticeService.plusNoticeViewCount(idx);

	// 	if(queryCount == 0)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "글 정보를 불러올 수 없습니다");
    //     	return "redirect:/";
	// 	}

	// 	List<Comment> comments = commentService.findComment(idx,BoardType.NOTICE);
	// 	if(authentication != null)
	// 	{
	// 		String username = authentication.getName();
    //     	User user = userService.findByUsername(username); 
    //     	model.addAttribute("writer", user.getWriter());
	// 	}
		
	// 	model.addAttribute("comments", comments);
	// 	return "noticeCheck/index";
	// }
	
	// @GetMapping("/notice-modify-page")
	// public String noticeModifyPage(@RequestParam("idx") int idx,Model model, RedirectAttributes redirectAttributes) {
	// 	if(idx == 0)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "글 정보를 불러올 수 없습니다");
    //     	return "redirect:/";
	// 	}

	// 	Notice notice = noticeService.findNotice(idx);

	// 	if(notice == null)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "글 정보를 불러올 수 없습니다");
    //     	return "redirect:/";
	// 	}

	// 	model.addAttribute("notice", notice);
	// 	return "noticeModify/index";
	// }

	@GetMapping("/my-page")
	public String myPage(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
		User user = userService.findByUsername(authentication.getName());

		if(user == null)
		{
			redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
        	return "redirect:/";
		}

		model.addAttribute("user", user);

		return "myPage/index";
	}

	@GetMapping("/find-password-page")
	public String findPasswordPage() {
		return "findPassword/index";
	}

	@GetMapping("/board-report-page")
	public String reportPage(@RequestParam int boardIdx, 
								@RequestParam int reportedUserIdx,
								@RequestParam String writer,
								@RequestParam String title,
								Authentication authentication, 
								RedirectAttributes redirectAttributes, 
								Model model) {
		User user = userService.findByUsername(authentication.getName());

		int userIdx = user.getIdx();
		
		//에러
		if(userIdx == 0)
		{
			redirectAttributes.addFlashAttribute("alreadyReported", true);
        	return "redirect:/dummy";
		}

		int reportCount = boardReportService.findBoardReportCount(userIdx, boardIdx);

		//이미 신고한 글
		if(reportCount != 0)
		{
			redirectAttributes.addFlashAttribute("alreadyReported", true);
        	return "redirect:/dummy";
		}
		
		model.addAttribute("alreadyReported", false);
		model.addAttribute("boardIdx", boardIdx);
		model.addAttribute("reportedUserIdx", reportedUserIdx);
		model.addAttribute("userIdx", userIdx);
		model.addAttribute("writer", writer);
		model.addAttribute("title", title);
		model.addAttribute("myWriter", user.getWriter());
		return "boardReport/index";
	}

	@GetMapping("/admin-page")
	public String adminPage(Model model) {
		List<User> allUser = userService.findAllUser();
		model.addAttribute("userList", allUser);
		return "admin/index";
	}
	
}
