/** */
package tech.pardus.multitenant.datasource.entity.audition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

  @Bean
  public AuditorAware<String> auditorAware() {
    return new EntityAuditorAware();
  }
}
