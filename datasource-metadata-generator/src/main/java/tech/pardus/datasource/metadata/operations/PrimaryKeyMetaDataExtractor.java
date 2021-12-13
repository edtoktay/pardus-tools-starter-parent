/**
 *
 */
package tech.pardus.datasource.metadata.operations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import tech.pardus.datasource.metadata.models.PrimaryKeys;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class PrimaryKeyMetaDataExtractor implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Set<PrimaryKeys> getAllPks(Connection connection, String catalog, String schema,
      String tableName) throws SQLException {
    var primaryKeys = new HashSet<PrimaryKeys>();
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getPrimaryKeys(catalog, schema, tableName);) {
      // @formatter:off
      primaryKeys.add(
          PrimaryKeys
            .builder()
              .columnName(resultSet.getString("COLUMN_NAME"))
              .keySequence(resultSet.getInt("KEY_SEQ"))
              .primaryKeyName(resultSet.getString("PK_NAME"))
            .build()
      );
      // @formatter:on
    }
    return primaryKeys;
  }
}
