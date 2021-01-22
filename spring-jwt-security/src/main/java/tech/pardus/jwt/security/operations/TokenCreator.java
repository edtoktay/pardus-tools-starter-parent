/** */
package tech.pardus.jwt.security.operations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.pardus.jwt.security.exceptions.TokenCreationException;
import tech.pardus.jwt.security.properties.JwtIssuerProperties;
import tech.pardus.utilities.PAsserts;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
@Component
@ConditionalOnBean(value = JwtIssuerProperties.class)
public class TokenCreator {

  @Autowired private JwtIssuerProperties jwtIssuerProperties;

  public JwtToken createToken(UserDetails context) {
    // @formatter:off
    PAsserts.hasText(
        context.getUsername(),
        () -> "issue_jwt_without_username",
        () -> TokenCreationException.class);
    PAsserts.notNull(
        context.getAuthorities(),
        () -> "issue_jwt_without_authorities",
        () -> TokenCreationException.class);
    var claims = Jwts.claims().setSubject(context.getUsername());
    claims.put(
        "roles",
        context
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::toString)
            .collect(Collectors.toList()));
    var now = LocalDateTime.now();
    var issueStartDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    var expiryDate =
        now.plus(jwtIssuerProperties.getExpirationTime(), jwtIssuerProperties.getExpiryTimeUnit());
    var issueExpiryDate = Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant());
    var token =
        Jwts.builder()
            .setClaims(claims)
            .setIssuer(jwtIssuerProperties.getIssuingAuthority())
            .setIssuedAt(issueStartDate)
            .setExpiration(issueExpiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtIssuerProperties.getSignKey())
            .compact();
    return JwtToken.builder()
        .token(token)
        .header(jwtIssuerProperties.getHeader())
        .headerPrefix(jwtIssuerProperties.getHeaderPrefix())
        .expireTime(expiryDate)
        .build();
    // @formatter:on
  }

  public JwtToken createToken(Authentication authentication) {
    var user =
        new User(authentication.getPrincipal().toString(), "", authentication.getAuthorities());
    return createToken(user);
  }

  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class JwtToken {

    private String token;

    private LocalDateTime expireTime;

    private String header;

    private String headerPrefix;
  }
}
