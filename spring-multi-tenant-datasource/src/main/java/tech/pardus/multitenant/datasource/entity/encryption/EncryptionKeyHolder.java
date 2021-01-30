/**
 *
 */
package tech.pardus.multitenant.datasource.entity.encryption;

import org.apache.commons.lang3.StringUtils;

/**
 * @author edtoktay
 *
 */
public class EncryptionKeyHolder {

	private static String encKey;

	public static String getEncryptionKey() {
		return StringUtils.isBlank(encKey) ? "Hedeleme_Hodolomo_Hebele" : encKey;
	}

	public static void setEncryptionKey(String encryptionKey) {
		encKey = encryptionKey;
	}

}
