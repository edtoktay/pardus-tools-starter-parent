/** */
package tech.pardus.jwt.security.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("security.jwt.token")
@ConditionalOnProperty(
    prefix = "security.jwt.token",
    name = "issuer",
    havingValue = "false",
    matchIfMissing = true)
public class JwtConsumerProperties implements JwtProperties {

  @Value("${security.jwt.tokenissuer:false}")
  private boolean issuer;

  private String signKey;

  private String header;

  private String headerPrefix;
}
