/** */
package tech.pardus.multitenant.datasource.entity.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class EncryptionCipher {

  private static final String CIPHER_INSTANCE_NAME = "AES/CBC/PKCS5Padding";

  private static final String SECRET_KEY_ALGORITHM = "AES";

  public Cipher configureAndGetInstance(int encryptionMode, String key)
      throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
          InvalidAlgorithmParameterException, InvalidAlgorithmParameterException {
    var cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
    var secretKey = new SecretKeySpec(key.getBytes(), SECRET_KEY_ALGORITHM);
    var ivBytes = new byte[cipher.getBlockSize()];
    var algorithmParameters = new IvParameterSpec(ivBytes);
    cipher.init(encryptionMode, secretKey, algorithmParameters);
    return cipher;
  }
}
