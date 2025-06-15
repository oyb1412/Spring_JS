package kr.co.myproject.controller;

import org.springframework.web.bind.annotation.RestController;

import kr.co.myproject.entity.BoardType;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Notice;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.CommentService;
import kr.co.myproject.service.NoticeService;
import kr.co.myproject.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class JSNoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

	private final Logger logger = LoggerFactory.getLogger(JSNoticeController.class);

    @GetMapping("/api/notice/list")
    public Map<String, Object> getNoticeList(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(required = false) String searchType,
                                             @RequestParam(required = false) String keyword) {

        Map<String, Object> result = new HashMap<>();
        List<Notice> noticeList = new ArrayList<>();
    	int pageSize = 10;
	 	int start = (page -1) * pageSize;
        int totalCount;


        if (keyword == null || keyword.trim().isEmpty()) {
    		keyword = null; 
    		searchType = null; 
		}

        if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
			noticeList = noticeService.searchNoticeListPaged(searchType, keyword, start, pageSize);
			totalCount = noticeService.countNoticeListByType(searchType, keyword); 
		} else {
			noticeList = noticeService.getPagedList(start, pageSize);
			totalCount = noticeService.getList().size();
		}

    	int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        result.put("noticeList", noticeList);
        result.put("currentPage", page);
        result.put("totalPage", totalPage);


        return result;
    }

    @GetMapping("/api/notice/check")
    public ResponseEntity<Map<String, Object>> getNoticeCheck(@RequestParam int idx, 
                                                               Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        Notice notice = noticeService.findNotice(idx);

        if(notice == null)
        {
            result.put("result", "글 정보를 불러올 수 없습니다");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        result.put("notice", notice);
        if(authentication != null  && authentication.isAuthenticated())
        {
            User user = userService.findByUsername(authentication.getName());
            result.put("user", user);
            result.put("auth", authentication.isAuthenticated());
        }

        List<Comment> comment = commentService.findComment(idx, BoardType.NOTICE);

        result.put("commentList", comment);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/notice/add")
    public Map<String, Object> noticeAdd(@RequestBody Notice notice,
                                          Authentication authentication)
    {
        Map<String, Object> result = new HashMap<>();
        
        if(notice == null)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }

        User user = userService.findByUsername(authentication.getName());

        LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String formattedNow = now.format(formatter);

        notice.setIndate(formattedNow);
        notice.setMemID(user.getUsername());
        notice.setWriter(user.getWriter());
        notice.setViewCount(0);
        notice.setCommentCount(0);
        notice.setUpCount(0);
        notice.setDownCount(0);

        int queryCount = noticeService.noticeInsert(notice);
        
        if(queryCount == 0)
        {
            result.put("error", "글 작성에 실패했습니다");
            return result;
        }

        result.put("success", "글 작성에 성공했습니다");
        return result;
    }

    @PostMapping("/api/notice/modify")
    public Map<String, Object> noticeModify(@RequestBody Notice notice) {
        Map<String, Object> result = new HashMap<>();
        
        if(notice == null)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }

        int queryCount = noticeService.noticeUpdate(notice);

        if(queryCount == 0)
        {
            result.put("error", "글 수정에 실패했습니다");
            return result;
        }
        
        result.put("success", "글 수정에 성공했습니다");
        return result;
    }

    @DeleteMapping("/api/notice/delete")
    public Map<String, Object> noticeDelete(@RequestParam int idx)
    {
        Map<String, Object> result = new HashMap<>();
        Notice notice = noticeService.findNotice(idx);

        if(notice == null)
        {
            result.put("error", "글 정보를 불러올 수 없습니다");
            return result;
        }

        if(noticeService.noticeDelete(idx) == 0)
        {
            result.put("error", "글 삭제에 실패했습니다");
            return result;
        }

        result.put("success", "글 삭제에 성공했습니다");
        return result;
    }
}
