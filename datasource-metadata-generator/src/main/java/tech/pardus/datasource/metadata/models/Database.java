/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class Database implements Serializable {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  private String catalogName;

  private Set<Schema> schemas;

  public Database(String catalogName) {
    super();
    this.catalogName = catalogName;
  }

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

  public Schema getSchema(String schemaName) {
    if (CollectionUtils.isEmpty(schemas)) {
      schemas = new HashSet<>();
    }
    var schema = schemas.stream().filter(t -> StringUtils.equals(t.getSchemaName(), schemaName))
        .findFirst().orElse(null);
    if (Objects.isNull(schema)) {
      schema = new Schema(schemaName);
      schemas.add(schema);
    }
    return schema;
  }

}
