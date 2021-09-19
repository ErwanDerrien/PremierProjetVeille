package com.backend.controller.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.controller.security.util.JwtTokenUtil;
import com.backend.service.UserPrincipalService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    JwtTokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        final String prefix = "Bearer ";

        if (requestTokenHeader != null && requestTokenHeader.startsWith(prefix)) {
            jwtToken = requestTokenHeader.substring(prefix.length());

            username = tokenUtil.getUsernameFromToken(jwtToken);

            // In this if condition, we verify that there's not already an user saved in the
            // context, which in our case is not possible as our application is statelesss
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userPrincipalService.loadUserByUsername(username);

                if (tokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }

            filterChain.doFilter(request, response);
        }

    }
}