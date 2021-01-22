/** */
package tech.pardus.multitenant.datasource.entity.encryption;

import javax.persistence.Converter;

import tech.pardus.multitenant.datasource.entity.converters.AbstractEncryptionConverter;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Converter(autoApply = false)
public class StringEncryptionConverter extends AbstractEncryptionConverter<String> {

  @Override
  public String convertStringToEntityAttribute(String dbData) {
    return dbData;
  }

  @Override
  public String convertEntityAttributeToString(String attribute) {
    return attribute;
  }
}
