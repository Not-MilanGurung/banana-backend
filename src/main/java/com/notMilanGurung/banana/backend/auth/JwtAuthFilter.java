package com.notMilanGurung.banana.backend.auth;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CookieService cookieService;

  public JwtAuthFilter(JwtService jwtService, CookieService cookieService) {
    this.jwtService = jwtService;
    this.cookieService = cookieService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                  FilterChain chain) throws ServletException, IOException {
    String token = cookieService.read(req, CookieService.ACCESS);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        Claims claims = jwtService.parse(token);
        if ("access".equals(claims.get("type", String.class))) {
          UUID id = UUID.fromString(claims.getSubject());
          String username = claims.get("username", String.class);

          AuthPrincipal principal = new AuthPrincipal(id, username);

          var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (JwtException | IllegalArgumentException ignored) {
        // invalid/expired: stay unauthenticated, entry point returns 401
      }
    }
    chain.doFilter(req, res);
  }
}