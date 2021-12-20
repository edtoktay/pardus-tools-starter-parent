/**
 *
 */
package tech.pardus.security;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author edtoktay
 * @since Dec 20, 2021
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan(basePackages = {"tech.pardus.security"})
public class SpringSecurityConfiguration {

}
