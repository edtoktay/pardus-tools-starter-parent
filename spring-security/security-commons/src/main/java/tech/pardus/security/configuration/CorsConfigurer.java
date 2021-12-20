/** */
package tech.pardus.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@Configuration
public class CorsConfigurer {

  @Bean
  public WebMvcConfigurer CORSConfigurer() {
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
            .maxAge(-1)
            .allowCredentials(false);
      }
    };
  }
}
