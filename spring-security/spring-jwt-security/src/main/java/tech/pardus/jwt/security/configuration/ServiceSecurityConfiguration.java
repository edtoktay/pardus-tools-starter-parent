/** */
package tech.pardus.jwt.security.configuration;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import tech.pardus.jwt.security.properties.JwtConsumerProperties;
import tech.pardus.security.configuration.TokenAuthenticationEntryPoint;
import tech.pardus.security.configuration.XSSFilter;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
// @formatter:off
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@ConditionalOnBean(value = JwtConsumerProperties.class)
// @formatter:on
public class ServiceSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf()
        .disable()
        .authorizeRequests()
        .accessDecisionManager(accessDecisionManager())
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(tokenAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // @formatter:on
  }

  @Bean
  public AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<? extends Object>> voters =
        Arrays.asList(new JwtAccessDecisionVoter());
    return new UnanimousBased(voters);
  }

  @Bean
  public FilterRegistrationBean<XSSFilter> xssPreventFilter() {
    var registrationBean = new FilterRegistrationBean<XSSFilter>();
    registrationBean.setFilter(new XSSFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}
