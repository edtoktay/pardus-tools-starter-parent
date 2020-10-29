/**
 *
 */
package tech.pardus.jwt.security;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan(basePackages = { "tech.pardus.jwt.security" })
public class SpringSecurityConfiguration {

	@Bean
	public String jwtSecurityInit() {
		return "OK";
	}

}
