package com.vibrantcovers.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
    
    public static String getUserId(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            userId = request.getHeader("X-User-Id");
        }
        return userId;
    }
    
    public static String getUserEmail(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            userEmail = request.getHeader("X-User-Email");
        }
        return userEmail;
    }
}





