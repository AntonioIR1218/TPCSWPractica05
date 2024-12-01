package org.uv.TPCSWPractica05;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil() {
        String keyString = System.getenv("JWT_SECRET_KEY");
        if (keyString == null || keyString.isEmpty()) {
            // Si no hay clave configurada, genera una clave segura (esto es temporal)
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            secretKey = Keys.hmacShaKeyFor(keyString.getBytes());
        }
    }
    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expira en 10 horas
                .signWith(secretKey)
                .compact();
    }

 
    public String generateToken(String username) {
        return generateToken(username, Map.of());
    }

  
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new RuntimeException("El token JWT ha expirado", e);
        } catch (io.jsonwebtoken.SignatureException e) {
            throw new RuntimeException("Firma JWT inválida", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el token JWT", e);
        }
    }

 
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (username.equals(extractedUsername) && !isTokenExpired(token));
    }

  
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
