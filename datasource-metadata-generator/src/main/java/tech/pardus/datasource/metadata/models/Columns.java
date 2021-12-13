/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import java.sql.Types;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Data
@Builder
public class Columns implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  private String columnName;
  private Types dataType;
  private String dataTypeName;
  private int columnSize;
  private Integer decimalDigits;
  private boolean nullable;
  private String remarks;
  private String columnDefault;
  private int ordinalPosition;
  private boolean autoIncrement;
  private Set<Relations> relations;
  private boolean primaryKey;
}
