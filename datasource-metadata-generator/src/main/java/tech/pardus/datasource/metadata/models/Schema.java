/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Schema implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  @NonNull
  private String schemaName;

  private Set<Tables> tables;

}
