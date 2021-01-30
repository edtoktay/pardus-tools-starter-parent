/**
 *
 */
package tech.pardus.multitenant.datasource.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import tech.pardus.multitenant.datasource.entity.encryption.EncryptionKeyHolder;

/**
 * @author edtoktay
 *
 */
@Configuration
public class KeyConfig {

	@Value("${encryption-key}")
	@Getter
	@Setter
	private String encryptionKey;

	@Bean
	public String keyHolder() {
		EncryptionKeyHolder.setEncryptionKey(encryptionKey);
		return "OK";
	}

}
