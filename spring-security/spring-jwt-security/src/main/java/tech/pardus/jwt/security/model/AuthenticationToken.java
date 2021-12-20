/** */
package tech.pardus.jwt.security.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {

  /** */
  private static final long serialVersionUID = 1L;

  @Getter private String token;

  public AuthenticationToken(Object principal, Object credentials) {
    super(principal, credentials);
  }

  public AuthenticationToken(String token) {
    super(null, null);
    this.token = token;
  }
}
