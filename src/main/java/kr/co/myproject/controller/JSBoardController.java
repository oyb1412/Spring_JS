package kr.co.myproject.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.myproject.entity.Board;
import kr.co.myproject.entity.BoardReport;
import kr.co.myproject.entity.BoardReported;
import kr.co.myproject.entity.BoardType;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Projects;
import kr.co.myproject.entity.ReportType;
import kr.co.myproject.entity.Resume;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.BoardReportService;
import kr.co.myproject.service.BoardReportedService;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.CommentService;
import kr.co.myproject.service.ProjectsService;
import kr.co.myproject.service.ResumeService;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class JSBoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BoardReportService boardReportService;

    @Autowired
    private BoardReportedService boardReportedService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ProjectsService projectsService;

    private final Logger logger = LoggerFactory.getLogger(JSBoardController.class);

    @PostMapping("/api/board/add")
    public Map<String, Object> boardAdd(@RequestBody Board board, 
                                         Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        
        if(board == null)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }

        if((board.getTitle() == null || board.getTitle().isEmpty())||
        (board.getContent() == null || board.getContent().isEmpty()))
        {
            result.put("error", "제목과 내용을 모두 작성해주세요");
            return result;
        }

        User user = userService.findByUsername(authentication.getName());
        board.setMemID(authentication.getName());
        board.setUserIdx(user.getIdx());
        board.setCommentCount(0);
        board.setViewCount(0);
        board.setUpCount(0);
        board.setDownCount(0);

        LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String formattedNow = now.format(formatter);
        board.setIndate(formattedNow);

        int queryCount = boardService.boardInsert(board);

        if(queryCount == 0)
        {
            result.put("error", "글 작성에 실패했습니다");
            return result;
        }
        
        result.put("success", "글 작성에 성공했습니다");
        return result;
    }
    

    @GetMapping("/api/board/list")
	public Map<String, Object> boardList(@RequestParam(name="page", defaultValue = "1") int page,
							             @RequestParam(name="searchType",required = false) String searchType,
							             @RequestParam(name="keyword",required = false) String keyword,
							             @RequestParam(name="sortField",required = false) String sortField,
							             @RequestParam(name="sortOrder",required = false) String sortOrder)
	{
        Map<String, Object> result = new HashMap<>();
		int pageSize = 10;
		List<Board> boardList = new ArrayList<>();
		int totalCount = 0;

		if (keyword == null || keyword.trim().isEmpty()) {
    		keyword = null; 
    		searchType = null; 
		}
		
		//1.검색만 있는 경우
		if ((searchType != null && keyword != null && !keyword.trim().isEmpty())
		&& (sortField == null || sortField.trim().isEmpty() || "default".equals(sortField))) {
			boardList = boardService.searchBoardListPaged(searchType, keyword);
		}
		//2.정렬만 있는 경우
		if((searchType == null || keyword == null || keyword.trim().isEmpty())
		&& sortField != null && !sortField.trim().isEmpty() && !"default".equals(sortField))
		{
			switch (sortField) {
				case "view":
					boardList = boardService.getBoardListDescViewCount();
					break;
				case "comment":
					boardList = boardService.getBoardListDescCommentCount();
					break;
				case "date":
					boardList = boardService.getBoardListIndate();
					break;
			}
			if("desc".equals(sortOrder))
			{
				Collections.reverse(boardList);
			}
		}

		//3.검색, 정렬 모두 해야하는 경우
		if((searchType != null && keyword != null && !keyword.trim().isEmpty()) 
		&& sortField != null && !sortField.trim().isEmpty() && !"default".equals(sortField))
		{
			switch (sortField) {
				case "view":
					boardList = boardService.searchBoardListPagedDescViewCount(searchType, keyword);
					break;
				case "comment":
					boardList = boardService.searchBoardListPagedDescCommentCount(searchType, keyword);
					break;
				case "date":
					boardList = boardService.searchBoardListPagedDescIndate(searchType, keyword);
					break;
			}
			if("desc".equals(sortOrder))
			{
				Collections.reverse(boardList);
			}

		}

		//4.검색, 정렬 아무것도 하지 않는 경우
		if((searchType == null || keyword == null || keyword.trim().isEmpty()) 
		&& (sortField == null || sortField.trim().isEmpty() || "default".equals(sortField)))
		{
			boardList = boardService.getList();
		}
			
		totalCount = boardList.size();
		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, boardList.size());
		boardList = boardList.subList(startIndex, endIndex);
    	int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        result.put("boardList", boardList);
        result.put("currentBoardPage", page);
        result.put("totalBoardPage", totalPage);
        result.put("searchType", searchType);
        result.put("keyword", keyword);

        return result;
	}

    @DeleteMapping("/api/board/delete")
    public Map<String, Object> boardDelete(@RequestParam int idx) {
        Map<String, Object> result = new HashMap<>();

        if(idx == 0)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }

        int queryCount = boardService.boardDelete(idx);

        if(queryCount == 0)
        {
            result.put("error", "글 삭제에 실패했습니다");
            return result;
        }

        result.put("success", "글 삭제에 성공했습니다");
        return result;

    }

    @GetMapping("/api/board/check")
    public Map<String, Object> boardCheck(@RequestParam int idx,
                                           Authentication authentication) {

        
        Map<String, Object> result = new HashMap<>();

        if(idx == 0)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }
        //보드 viewCount 올리기
        boardService.plusBoardViewCount(idx);

        Board board = boardService.findBoard(idx);

        if(board == null)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }

         List<Comment> comment = commentService.findComment(idx, BoardType.FREE);

        if(authentication != null && authentication.isAuthenticated())
        {
            User user = userService.findByUsername(authentication.getName());
            result.put("user", user);
        }

        

        result.put("commentList", comment);

        result.put("board", board);
        return result;
    }
    
    @PostMapping("/api/board/modify")
    public Map<String, Object> boardModify(@RequestBody Board board) {
        Map<String, Object> result = new HashMap<>();
        
        if(board == null)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
            return result;
        }

        if((board.getTitle() == null || board.getTitle().isEmpty())||
        (board.getContent() == null || board.getContent().isEmpty()))
        {
            result.put("error", "제목과 내용을 모두 작성해 주세요");
        	return result;
        }
        logger.info("board.title : " + board.getTitle());
        logger.info("board.content : " + board.getContent());

        int queryCount = boardService.boardUpdate(board);

        if(queryCount == 0)
        {
            result.put("error", "글 수정에 실패했습니다");
        	return result;
        }

            result.put("success", "글 수정에 성공했습니다");
        	return result;
    }
    
    @GetMapping("/api/board/report/data")
    public Map<String, Object> boardReportData(@RequestParam int idx, 
                                                Authentication authentication) {
        Map<String, Object> result = new HashMap<>();
        
        //현재 글 idx(idx)
        //신고당하는 유저 idx(글 idx로 name찾아서 찾아야함)
        //현재 글 작성 유저 닉네임(글 idx로 name찾아서 찾아야함)
        //현재 글 제목(글 idx로 찾아야함)
        Board currentBoard = boardService.findBoard(idx);
        User user = userService.findByUsername(authentication.getName());

        result.put("board", currentBoard);
        result.put("user", user);

        return result;
    }

    @PostMapping("/api/board/report")
        public Map<String, Object> BoardReport(@RequestParam("boardIdx") int boardIdx,
                                               @RequestParam("reportType") ReportType reportType,
                                                Authentication authentication)
        {
            Map<String, Object> result = new HashMap<>();

            int userIdx = userService.findByUsername(authentication.getName()).getIdx();

            String repotedUsername = boardService.findBoard(boardIdx).getMemID();

            int reportedUserIdx = userService.findByUsername(repotedUsername).getIdx();

            BoardReport boardReport = new BoardReport();
            boardReport.setBoardIdx(boardIdx);
            boardReport.setUserIdx(userIdx);
            boardReport.setReportType(reportType);

	        LocalDateTime now = LocalDateTime.now();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	    String formattedNow = now.format(formatter);

            logger.info("1");

            boardReport.setIndate(formattedNow);
            int queryCount = boardReportService.insertBoardReport(boardReport);

            if (userIdx == reportedUserIdx) {
                result.put("error", "자기 자신은 신고할 수 없습니다.");
                return result;
            }

            //에러
            if(queryCount == 0)
            {
                result.put("error", "신고 정보가 올바르지 않습니다");
                return result;
            }

            //해당유저 신고받은 전적 체크
            Integer reportedCount = boardReportedService.findBoardReportedCount(reportedUserIdx, reportType);

            //전적이 없다면 새롭게 추가
            if(reportedCount == null || reportedCount == 0)
            {
                BoardReported boardReported = new BoardReported();
                boardReported.setBoardIdx(boardIdx);
                boardReported.setCount(1);
                boardReported.setReportType(reportType);
                boardReported.setIndate(formattedNow);
                boardReported.setUserIdx(reportedUserIdx);
                boardReportedService.insertBoardReported(boardReported);
            }
            else
            {
                //전적이 있다면 해당 유저 리포트 카운트 +1
                boardReportedService.plusBoardReportedCount(reportedUserIdx, reportType);

                //해당 유저 리포트 횟수가 4회(count+1전 기록)이면 정지
                if(reportedCount > 4)
                {
                    userService.UpdateBan(true, reportedUserIdx);
                }
            }
            
            result.put("success", "유저 신고에 성공했습니다");
            return result;
        }
    
    @GetMapping("/api/resume")
    public Map<String, Object> ResumeBoardCheckPage() {
        Map<String, Object> result = new HashMap<>();

        Resume resume = resumeService.SelectResume(2);

        result.put("resume", resume);
        return result;
    }

    @GetMapping("/api/projects/list")
    public Map<String, Object> ProjectsBoardListPage() {
        Map<String, Object> result = new HashMap<>();
        List<Projects> projectsList = projectsService.SelectProjects();
        
        result.put("projectsList", projectsList);
        return result;
    }

    @GetMapping("/api/project/check")
    public Map<String, Object> getMethodName(@RequestParam int idx) {
        Map<String, Object> result = new HashMap<>();

        Projects project = projectsService.SelectProjectsByIdx(idx);

        result.put("project", project);
        return result;
    }
    
}
