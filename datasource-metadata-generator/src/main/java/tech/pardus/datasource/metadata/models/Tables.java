/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Data
@Builder
public class Tables implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  private String tableName;
  private TableType tableType;
  private String remarks;
  private Set<Columns> columns;
  private Set<PrimaryKeys> primaryKeys;
}
