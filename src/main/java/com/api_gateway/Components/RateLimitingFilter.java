package com.api_gateway.Components;


import com.api_gateway.Service.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{
        String apiKey=request.getHeader("X-API-KEY");

        boolean allowed= rateLimiter.allowRequest(apiKey);

        if(!allowed){
            response.setStatus(429);
            response.getWriter().write("Rate limit exceeded or invalid API key");
            return;
        }

        filterChain.doFilter(request,response);
    }
}
