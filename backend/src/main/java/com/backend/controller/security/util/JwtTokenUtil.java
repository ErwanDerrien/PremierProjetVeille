package com.backend.controller.security.util;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Configuration
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185432626007488L;

    private static final Logger log = Logger.getLogger(JwtTokenUtil.class.getName());

    @Value("${jwt.validity.seconds:300}")
    private Long validityInSeconds;

    @Value("${jwt.secret:}")
    private String _secretBase;

    private Key _secretKey;

    private String getSecretBase() {
        // Read:
        // https://stackoverflow.com/questions/40252903/static-secret-as-byte-key-or-string/40274325#40274325
        if (_secretBase == null || _secretBase.length() == 0) {
            Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            _secretBase = Encoders.BASE64.encode(key.getEncoded());
            log.warning("\n-- Temporary key base for the JWT: " + _secretBase
                    + "\n   Only valid for a single server instance environment (as all server instances will end-up having their own unique JWT key)."
                    + "\n   Fix the issue by documenting the Base64 encoded secret key base as `jwt.secret` in the `application.properties` file.");

        }
        return _secretBase;
    }

    private Key getSecretKey() {
        if (_secretKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(getSecretBase());
            _secretKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return _secretKey;
    }

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretBase()).build().parseClaimsJws(token).getBody();
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // generate a URL-safe and compact JWT token
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuer("https://backend.com/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInSeconds * 1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS512).compact();
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
