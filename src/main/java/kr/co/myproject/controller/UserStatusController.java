package kr.co.myproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.UserService;



@RestController
public class UserStatusController {
    @Autowired
    private UserService userService;

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