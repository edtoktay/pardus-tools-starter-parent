/** */
package tech.pardus.security.configuration;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  /** */
  private static final long serialVersionUID = -6267761096550084715L;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized_access");
  }
}
