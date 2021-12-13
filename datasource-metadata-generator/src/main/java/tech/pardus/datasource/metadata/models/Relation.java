/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Data
@Builder
public class Relation implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String category;
  private String schema;
  private String tableName;
  private String columnName;
}
