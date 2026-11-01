package com.notMilanGurung.banana.backend.auth;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final SecretKey key;
  private final long accessExp;
  private final long refreshExp;

  public JwtService(@Value("${jwt.secret}") String secret,
      @Value("${jwt.access-expiration-ms}") long accessExp,
      @Value("${jwt.refresh-expiration-ms}") long refreshExp) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.accessExp = accessExp;
    this.refreshExp = refreshExp;
  }

  public String generateAccessToken(String username) {
    return build(username, accessExp, "access");
  }

  public String generateRefreshToken(String username) {
    return build(username, refreshExp, "refresh");
  }

  private String build(String subject, long exp, String type) {
    Date now = new Date();
    return Jwts.builder()
        .subject(subject)
        .claim("type", type)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + exp))
        .signWith(key)
        .compact();
  }

  /** Throws JwtException if invalid or expired */
  public Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload();
  }
}