package com.notMilanGurung.banana.backend.auth;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
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

  public String generateAccessToken(UUID id, String username) {
    return build(id, username, accessExp, "access");
  }

  public String generateRefreshToken(UUID id) {
    return build(id, null, refreshExp, "refresh");
  }

  private String build(UUID subject, String username, long exp, String type) {
    Date now = new Date();
    final JwtBuilder builder = Jwts.builder()
        .subject(subject.toString())
        .claim("type", type)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + exp))
        .signWith(key);
    
    if (username != null) {
      builder.claim("username", username);
    }

    return builder.compact();
  }

  /** Throws JwtException if invalid or expired */
  public Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload();
  }
}