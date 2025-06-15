package kr.co.myproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class JSCsrfController {

    @GetMapping("/api/csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        Map<String, String> result = new HashMap<>();
        result.put("token", token.getToken());
        result.put("parameterName", token.getParameterName());
        result.put("headerName", token.getHeaderName());
        return result;
    }
}
