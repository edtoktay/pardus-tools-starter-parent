package tech.pardus.multitenant.datasource.entity.converters;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.pardus.multitenant.datasource.entity.encryption.EncryptionCipher;
import tech.pardus.multitenant.datasource.entity.encryption.EncryptionKeyHolder;
import tech.pardus.utilities.PAsserts;

/**
 * @author deniz.toktay
 * @param <T>
 * @since Dec 29, 2020
 */
public abstract class AbstractEncryptionConverter<T> implements AttributeConverter<T, String> {

	private static final Logger log = LoggerFactory.getLogger(AbstractEncryptionConverter.class);

	private EncryptionCipher cipherMaker;

	public AbstractEncryptionConverter(EncryptionCipher cipherMaker) {
		this.cipherMaker = cipherMaker;
	}

	public AbstractEncryptionConverter() {
		this(new EncryptionCipher());
	}

	@Override
	public String convertToDatabaseColumn(T attribute) {
		PAsserts.hasText(EncryptionKeyHolder.getEncryptionKey(), () -> "no_encryption_key");
		if (Objects.nonNull(attribute)) {
			try {
				var cipher = cipherMaker.configureAndGetInstance(Cipher.ENCRYPT_MODE,
				        EncryptionKeyHolder.getEncryptionKey());
				return encryptData(cipher, attribute);
			} catch (InvalidKeyException e) {
				log.error("db_encryption_error", e);
			} catch (NoSuchPaddingException e) {
				log.error("db_encryption_error", e);
			} catch (NoSuchAlgorithmException e) {
				log.error("db_encryption_error", e);
			} catch (InvalidAlgorithmParameterException e) {
				log.error("db_encryption_error", e);
			} catch (IllegalBlockSizeException e) {
				log.error("db_encryption_error", e);
			} catch (BadPaddingException e) {
				log.error("db_encryption_error", e);
			}
		}
		return convertEntityAttributeToString(attribute);
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		PAsserts.hasText(EncryptionKeyHolder.getEncryptionKey(), () -> "no_encryption_key");
		if (StringUtils.isNotBlank(dbData)) {
			try {
				var cipher = cipherMaker.configureAndGetInstance(Cipher.DECRYPT_MODE,
				        EncryptionKeyHolder.getEncryptionKey());
				return decryptData(cipher, dbData);
			} catch (InvalidKeyException e) {
				log.error("db_encryption_error", e);
			} catch (NoSuchPaddingException e) {
				log.error("db_encryption_error", e);
			} catch (NoSuchAlgorithmException e) {
				log.error("db_encryption_error", e);
			} catch (InvalidAlgorithmParameterException e) {
				log.error("db_encryption_error", e);
			} catch (IllegalBlockSizeException e) {
				log.error("db_encryption_error", e);
			} catch (BadPaddingException e) {
				log.error("db_encryption_error", e);
			}
		}
		return convertStringToEntityAttribute(dbData);
	}

	private String encryptData(Cipher cipher, T attribute) throws IllegalBlockSizeException, BadPaddingException {
		var bytesToEncrypt = convertEntityAttributeToString(attribute).getBytes();
		var encryptedBytes = cipher.doFinal(bytesToEncrypt);
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	private T decryptData(Cipher cipher, String dbData) throws IllegalBlockSizeException, BadPaddingException {
		var bytesToDecrypt = Base64.getDecoder().decode(dbData);
		var decryptedBytes = cipher.doFinal(bytesToDecrypt);
		return convertStringToEntityAttribute(new String(decryptedBytes));
	}

	public abstract T convertStringToEntityAttribute(String dbData);

	public abstract String convertEntityAttributeToString(T attribute);

}
