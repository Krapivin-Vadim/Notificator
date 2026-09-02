package org.vadim.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vadim.config.security.port.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if(header == null || !header.startsWith(BEARER_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }
        var jwt = header.substring(BEARER_PREFIX.length());
        var userId = jwtService.extractUserId(jwt);
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            var details = userDetailsService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    details,
                    null,
                    details.getAuthorities()
            );
            token.setDetails(details);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
