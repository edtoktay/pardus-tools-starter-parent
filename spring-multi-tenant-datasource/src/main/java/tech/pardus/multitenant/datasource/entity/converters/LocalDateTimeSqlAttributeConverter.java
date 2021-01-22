/** */
package tech.pardus.multitenant.datasource.entity.converters;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Converter(autoApply = true)
public class LocalDateTimeSqlAttributeConverter
    implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
    return attribute == null ? null : Timestamp.valueOf(attribute);
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
    return dbData == null ? null : dbData.toLocalDateTime();
  }
}
