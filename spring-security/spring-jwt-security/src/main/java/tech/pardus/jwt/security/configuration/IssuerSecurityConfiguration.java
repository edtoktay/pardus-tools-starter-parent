/** */
package tech.pardus.jwt.security.configuration;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import tech.pardus.jwt.security.properties.JwtIssuerProperties;
import tech.pardus.security.configuration.AuthorizationRequestFilter;
import tech.pardus.security.configuration.XSSFilter;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
// @formatter:off
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@ConditionalOnBean(value = JwtIssuerProperties.class)
// @formatter:on
public class IssuerSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Autowired
  private JwtIssuerProperties jwtIssuerProperties;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return new ProviderManager(Arrays.asList(authenticationProvider));
  }

  @Bean
  public AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<? extends Object>> voters =
        Arrays.asList(new JwtAccessDecisionVoter());
    return new UnanimousBased(voters);
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    // @formatter:off
    httpSecurity
        .csrf()
        .disable()
        .exceptionHandling()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(jwtIssuerProperties.getLoginUrl())
        .permitAll()
        .anyRequest()
        .authenticated()
        .accessDecisionManager(accessDecisionManager());
    httpSecurity
        .authenticationProvider(authenticationProvider)
        .addFilterAfter(
            new AuthorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    // @formatter:on
  }

  @Bean
  public FilterRegistrationBean<XSSFilter> xssPreventFilter() {
    var registrationBean = new FilterRegistrationBean<XSSFilter>();
    registrationBean.setFilter(new XSSFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}
