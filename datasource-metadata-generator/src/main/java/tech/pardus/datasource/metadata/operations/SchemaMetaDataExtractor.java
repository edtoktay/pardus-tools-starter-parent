/**
 *
 */
package tech.pardus.datasource.metadata.operations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import tech.pardus.datasource.metadata.models.MetaDataConstants;
import tech.pardus.datasource.metadata.models.Schema;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class SchemaMetaDataExtractor implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Set<Schema> getAllSchemas(Connection connection) throws SQLException {
    return getAllSchemas(connection, null);
  }

  public Set<Schema> getAllSchemas(Connection connection, String catalogName) throws SQLException {
    var schemas = new HashSet<Schema>();
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getSchemas(catalogName, null)) {
      while (resultSet.next()) {
        schemas.add(new Schema(resultSet.getString("TABLE_SCHEM")));
      }
    }
    return schemas;
  }

  public Set<Schema> getAllUserSchemas(Connection connection) throws SQLException {
    return getAllUserSchemas(connection, null);
  }

  public Set<Schema> getAllUserSchemas(Connection connection, String catalogName)
      throws SQLException {
    var schemas = new HashSet<Schema>();
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getSchemas(catalogName, null)) {
      while (resultSet.next()) {
        var schemaName = resultSet.getString("TABLE_SCHEM");
        if (!MetaDataConstants.reservedSchemas.contains(schemaName)) {
          schemas.add(new Schema(schemaName));
        }
      }
    }
    return schemas;
  }

}
