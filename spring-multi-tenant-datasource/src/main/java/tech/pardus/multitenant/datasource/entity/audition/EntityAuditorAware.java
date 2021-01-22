/** */
package tech.pardus.multitenant.datasource.entity.audition;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;

import tech.pardus.utilities.SessionUserContextHolder;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class EntityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(getCurrentUser());
  }

  private String getCurrentUser() {
    String user = "SYSTEM";
    try {
      var sessionUser = SessionUserContextHolder.getCurrentSessionUser();
      if (StringUtils.isNotBlank(sessionUser)) {
        user = sessionUser;
      }
    } catch (Exception e) {
    }
    return user;
  }
}
