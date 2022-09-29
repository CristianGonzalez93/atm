package com.atm.util;

import com.atm.dto.UserDetailsDTO;
import com.atm.exception.InvalidCredentialsException;
import com.atm.repository.AccountRepository;
import com.atm.repository.domain.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtUtils {

  public final static String USER_ID = "id";
  private final String SECRET_KEY = "secret";

  @Autowired
  private AccountRepository accountRepository;

  public String extractUserId(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Object extractClaim(String token, String claim) {
    return extractAllClaims(token).get(claim);
  }

  public String generateToken(UserDetailsDTO userDetails) {
    return generateTokenWithFields(userDetails, new HashMap<>(), 10L);
  }

  private String generateTokenWithFields(UserDetailsDTO userDetails, Map<String, Object> claims, Long duration) {
    claims.put(USER_ID, userDetails.getId());
    return createToken(claims, Long.toString(userDetails.getId()), duration);
  }

  public Account validateToken(String token) {
    String id = extractUserId(token);
    Optional<Account> optionalAccount = accountRepository.findById(Long.parseLong(id));

    if (optionalAccount.isPresent()) {
      if (!validateToken(token, new UserDetailsDTO(optionalAccount.get()))) {
        throw new ExpiredJwtException(null, null, "Expired jwt");
      } else {
        return optionalAccount.get();
      }
    } else {
      throw new InvalidCredentialsException("Invalid credentials");
    }
  }

  public Boolean validateToken(String token, UserDetailsDTO userDetailsDTO) {
    final String userId = extractUserId(token);
    return (userId.equals(Long.toString(userDetailsDTO.getId())) && !isTokenExpired(token));
  }

  private String createToken(Map<String, Object> claims, String subject, Long hoursDuration) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * hoursDuration))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }
}
