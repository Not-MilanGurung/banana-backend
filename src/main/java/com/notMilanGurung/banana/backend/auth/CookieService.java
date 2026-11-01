package com.notMilanGurung.banana.backend.auth;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieService {

  public static final String ACCESS = "access_token";
  public static final String REFRESH = "refresh_token";

  @Value("${jwt.access-expiration-ms}") private long accessExp;
  @Value("${jwt.refresh-expiration-ms}") private long refreshExp;
  @Value("${cookie.secure}") private boolean secure;

  public void addAuthCookies(HttpServletResponse res, String access, String refresh) {
    res.addHeader(HttpHeaders.SET_COOKIE, build(ACCESS, access, "/", accessExp).toString());
    res.addHeader(HttpHeaders.SET_COOKIE, build(REFRESH, refresh, "/", refreshExp).toString());
  }

  public void clearAuthCookies(HttpServletResponse res) {
    res.addHeader(HttpHeaders.SET_COOKIE, build(ACCESS, "", "/", 0).toString());
    res.addHeader(HttpHeaders.SET_COOKIE, build(REFRESH, "", "/", 0).toString());
  }

  public String read(HttpServletRequest req, String name) {
    if (req.getCookies() == null) return null;
    for (Cookie c : req.getCookies()) {
      if (c.getName().equals(name)) return c.getValue();
    }
    return null;
  }

  private ResponseCookie build(String name, String value, String path, long maxAgeMs) {
    return ResponseCookie.from(name, value)
        .httpOnly(true)
        .secure(secure)
        .sameSite("Strict")
        .path(path)
        .maxAge(Duration.ofMillis(maxAgeMs))
        .build();
  }
}