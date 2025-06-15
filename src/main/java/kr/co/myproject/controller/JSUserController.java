package kr.co.myproject.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.entity.Role;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class JSUserController {
    
    @Autowired
    private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final Logger logger = LoggerFactory.getLogger(JSUserController.class);

	@PostMapping("/api/admin/ban")
	public Map<String, Object> adminBan(@RequestParam("idx") int idx,
										@RequestParam("ban") boolean ban) {
        Map<String, Object> result = new HashMap<>();
		logger.info("1");
		User user = userService.findByIdx(idx);

		if(user == null)
		{
			result.put("error", "유저 정보가 올바르지 않습니다");
			return result;
		}


		if(user.isBan() == ban)
		{
			if(ban)
			{
				result.put("error", "이미 정지당한 유저입니다");
				return result;
			}
			else
			{
				result.put("error", "정지당하지 않은 유저입니다");
				return result;
			}
		}


		int queryCount = userService.UpdateBan(ban, idx);

		if(queryCount == 0)
		{
			result.put("error", "유저 정보가 올바르지 않습니다");
			return result;
		}

		if(ban)
		{
			result.put("success", "해당 유저를 정지시켰습니다");
		}
		else
		{
			result.put("success", "해당 유저의 정지를 해제했습니다");

		}

		return result;
	}

	@GetMapping("/api/admin/data")
	public Map<String, Object> adminPage() {
        Map<String, Object> result = new HashMap<>();

		List<User> userList = userService.findAllUser();

		result.put("userList", userList);
		return result;
	}

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> postRegister(@RequestParam("username") String username,
                               								@RequestParam("password") String password,
							   								@RequestParam("writer") String writer) {

        Map<String, Object> result = new HashMap<>();

        if(username == null || username.isEmpty())
		{
			result.put("result", "아이디를 적어주세요");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}

        if(password == null || password.isEmpty())
		{
			result.put("result", "비밀번호를 적어주세요");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}

		if(writer == null || writer.isEmpty())
		{
			result.put("result", "닉네임을 적어주세요");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
		}

		if(userService.countByUsername(username) > 0) {
        	result.put("result", "이미 존재하는 아이디입니다");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    	}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String formattedNow = now.format(formatter);

        User user = new User();
		user.setIndate(formattedNow);
		user.setRole(Role.MEMBER);
		String passwordEncoded = passwordEncoder.encode(password);
		user.setUsername(username);
		user.setPassword(passwordEncoded);
		user.setWriter(writer);
		int queryCount = userService.insertUser(user);

		if(queryCount == 0)
		{
			result.put("result", "알 수 없는 오류로 회원가입에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}

		result.put("result", "회원가입에 성공했습니다");
            return ResponseEntity.ok(result);
    }

	@GetMapping("/api/user/data")
	public Map<String, Object> userData(Authentication authentication) {
		Map<String, Object> result = new HashMap<>();

		if(authentication != null && authentication.isAuthenticated())
		{
			User user = userService.findByUsername(authentication.getName());
			result.put("success", "유저 정보 취득 성공");
			result.put("user", user);
			return result;
		}
		else
		{
			result.put("error", "유저 정보 취득 실패");
			return result;

		}
	}
	
	@PostMapping("/api/user/findPassword")
	public Map<String, Object> findPassword(@RequestBody User user,
											@RequestParam("passwordConfirm") String passwordConfirm) {
												
		Map<String, Object> result = new HashMap<>();

		if(user == null ||
		(passwordConfirm == null || passwordConfirm.isEmpty()))
		{
			result.put("error", "입력 정보가 올바르지 않습니다");
			return result;
		}

		if (!user.getPassword().equals(passwordConfirm)) {
        	result.put("error", "비밀번호가 서로 다릅니다");
			return result;
    	}

		String passwordEncoded = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordEncoded);
		int queryCount = userService.updatePassword(user);

		if(queryCount == 0)
		{
			result.put("error", "비밀번호 변경 실패");
			return result;
		}

		result.put("success", "비밀번호 변경 성공");
		return result;
	}

	@PostMapping("api/userdata/modify")
	public Map<String, Object> userdataModify(@RequestBody User user) {
		Map<String, Object> result = new HashMap<>();

		//유저 값 예외처리
		if(user == null )
		{
			result.put("error", "유저 정보가 올바르지 않습니다");
			return result;
		}
		
		//모든 칸이 비워져있나 체크
		if((user.getPassword() == null || user.getPassword().isEmpty()) && 
		(user.getWriter() == null || user.getWriter().isEmpty()))
		{
			result.put("error", "수정할 내용을 적어주세요");
			return result;
		}

		boolean success = true;

		//비밀번호를 수정하는 상황
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
    		String userPassword = user.getPassword();
			String passwordEncoded = passwordEncoder.encode(userPassword);
			user.setPassword(passwordEncoded);

			//쿼리 성공 예외처리
			int updateQuery = userService.updatePassword(user);

			if(updateQuery == 0)
			{
				//쿼리 실패
				success = false;
			}
		}

		//작성자 수정 쿼리
		if (user.getWriter() != null && !user.getWriter().isEmpty()) {
			//쿼리 성공 예외처리
			int updateQuery = userService.UpdateWriter(user);
			
			if(updateQuery == 0)
			{
				//쿼리 실패
				success = false;
			}
		}

		if(!success)
		{
			result.put("error", "수정에 실패했습니다");
			return result;
		}

		//마이페이지 수정 완료
		result.put("success", "수정에 성공했습니다");
		return result;
	}

	@GetMapping("/api/user")
    public Map<String, Object> getUser(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();

        if(authentication == null || !authentication.isAuthenticated())
        {
            result.put("error", "유저 정보가 올바르지 않습니다");
            return result;
        }

        User user = userService.findByUsername(authentication.getName());

        if(user == null)
        {
            result.put("error", "유저 정보가 올바르지 않습니다");
            return result;
        }

        result.put("user", user);
        return result;
    }
    
    @GetMapping("/api/user/status")
    public Map<String, Object> getStatus(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("ADMIN");
        Boolean isManager = (Boolean) session.getAttribute("MANAGER");

        result.put("isAuthenticated", isAuthenticated != null && isAuthenticated);
        result.put("username", username != null ? username : "");
        result.put("admin", isAdmin != null && isAdmin);
        result.put("manager", isManager != null && isManager);


        return result;
    }

    @GetMapping("/api/user/adminOrManager")
    public Map<String, String> getAdminOrManager(HttpSession session) {
        Map<String, String> result = new HashMap<>();

        if (session.getAttribute("MEMBER") == null &&
            session.getAttribute("ADMIN") == null &&
            session.getAttribute("MANAGER") == null) {
            result.put("user", "GUEST"); // 또는 "GUEST" 등
            return result;
        }

        if(session.getAttribute("MEMBER") != null  &&(boolean)session.getAttribute("MEMBER") == true)
        {
            result.put("user", "MEMBER");
        }
        else if(session.getAttribute("ADMIN") != null && (boolean)session.getAttribute("ADMIN") == true)
        {
            result.put("user", "ADMIN");
        }
        else if(session.getAttribute("MANAGER") != null)
        {
            result.put("user", "MANAGER");
        }

        return result;
    }
}
