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
public class PrimaryKeys implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  private String columnName;
  private int keySequence;
  private String primaryKeyName;
}
