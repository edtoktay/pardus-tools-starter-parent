/** */
package tech.pardus.jwt.security.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@Component
@Primary
public class IssuerAuthenticationProvider implements AuthenticationProvider {

  // @formatter:off
  private static List<TestUser> users =
      Arrays.asList(
          new TestUser(
              "Test1",
              "1234",
              new String[] {"ADMIN", "AGENT"},
              new String[] {"test_key_1", "test_key_2"}),
          new TestUser(
              "Test2", "5678", new String[] {"USER"}, new String[] {"test_key_1", "test_key_3"}));
  // @formatter:on

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    Object credentials = authentication.getCredentials();
    System.out.println("credentials class: " + credentials.getClass());
    if (!(credentials instanceof String)) {
      return null;
    }
    String password = credentials.toString();
    Optional<TestUser> userOptional =
        users.stream().filter(u -> u.match(name, password)).findFirst();
    if (!userOptional.isPresent()) {
      throw new BadCredentialsException("Authentication failed for " + name);
    }
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.addAll(
        Stream.of(userOptional.get().getAccessKeys())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()));
    grantedAuthorities.addAll(
        Stream.of(userOptional.get().getRoles())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()));
    Authentication auth =
        new UsernamePasswordAuthenticationToken(name, password, grantedAuthorities);
    return auth;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class TestUser {

    private String name;

    private String password;

    private String[] roles;

    private String[] accessKeys;

    public boolean match(String name, String password) {
      return this.name.equals(name) && this.password.equals(password);
    }
  }
}
