package com.hotel.booking.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtService {



    private static final String secret = "HeUWccnXkUgNMLa7z960a0cxUZXhSDE2Z0GM9dN6kxhoMjMXNRXtiIsGMXwcmN4t5PNSroQ1lNMgLTDFJ7iCLr4GIqQTdpwHc98Uhd8jnNpT2I1jMs4wjuqFOnP8FShM";

//    private static final String secretString = Base64.getEncoder().encodeToString(secret.getEncoded());




    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(String userName, String role){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName, role);
    }

    private String createToken(Map<String, Object> claims, String userName, String role) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .claim("role", role) // Thêm vai trò vào claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30000))
                .signWith(getSignKey(), SignatureAlgorithm.HS512).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}