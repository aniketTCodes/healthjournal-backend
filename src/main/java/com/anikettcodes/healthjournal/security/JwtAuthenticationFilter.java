package com.anikettcodes.healthjournal.security;

import com.anikettcodes.healthjournal.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService; // Your class where generate/validate token logic exists

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 🔹 Step 1: Get Authorization header
        final String authHeader = request.getHeader("Authorization");

        String jwt = null;
        String username = null;

        // 🔹 Step 2: Check if header contains Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // remove "Bearer "

            // 🔹 Step 3: Extract username from token
            username = jwtService.extractUsername(jwt);
        }

        // 🔹 Step 4: If username exists and user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 🔹 Step 5: Load user details from DB (or in-memory)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 🔹 Step 6: Validate token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 🔹 Step 7: Create authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,      // principal (user)
                                null,             // credentials (null because already verified)
                                userDetails.getAuthorities() // roles
                        );

                // 🔹 Step 8: Attach request details (IP, session, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 🔹 Step 9: Set authentication in SecurityContext
                // This tells Spring Security: "user is authenticated"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 🔹 Step 10: Continue filter chain
        filterChain.doFilter(request, response);
    }
}
