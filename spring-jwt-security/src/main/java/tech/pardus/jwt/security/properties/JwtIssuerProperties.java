/**
 *
 */
package tech.pardus.jwt.security.properties;

import java.time.temporal.ChronoUnit;

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
@ConditionalOnProperty(prefix = "security.jwt.token", name = "issuer", havingValue = "true", matchIfMissing = false)
public class JwtIssuerProperties implements JwtProperties {

	@Value("${security.jwt.tokenissuer:false}")
	private boolean issuer;

	private int expirationTime;

	@Value("${security.jwt.token.expiry-time-unit:MINUTES}")
	private ChronoUnit expiryTimeUnit;

	private String issuingAuthority;

	private String signKey;

	private String header;

	private String headerPrefix;

	private String loginUrl;

}
