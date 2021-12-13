/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// @RequiredArgsConstructor
public class Database implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  // @NonNull
  private String catalogName;

  private Set<Schema> schemas;

  public void add(Schema schema) {
    if (CollectionUtils.isEmpty(schemas)) {
      schemas = new HashSet<>();
    }
    schemas.add(schema);
  }

  public void add(String schemaName) {
    if (CollectionUtils.isEmpty(schemas)) {
      schemas = new HashSet<>();
    }
    schemas.add(new Schema(schemaName));
  }

  public Database(String catalogName) {
    super();
    this.catalogName = catalogName;
  }
}
