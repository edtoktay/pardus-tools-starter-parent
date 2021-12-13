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
import lombok.Data;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Data
public class Databases implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Set<Database> databases;

  public void add(String dbName) {
    if (CollectionUtils.isEmpty(databases)) {
      databases = new HashSet<>();
    }
    databases.add(new Database(dbName));
  }

  public Database getDataBase(String dbName) {
    if (CollectionUtils.isEmpty(databases)) {
      databases = new HashSet<>();
    }
    var database = databases.stream().filter(t -> StringUtils.equals(t.getCatalogName(), dbName))
        .findFirst().orElse(null);
    if (Objects.isNull(database)) {
      database = new Database(dbName);
      databases.add(database);
    }
    return database;
  }
}
