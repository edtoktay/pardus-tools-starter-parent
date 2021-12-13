/**
 *
 */
package tech.pardus.datasource.metadata.operations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import tech.pardus.datasource.metadata.models.Databases;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class CatalogMetaDataExtractor implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public void getAllCatalogs(Connection connection, Databases databases) throws SQLException {
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getCatalogs()) {
      if (Objects.isNull(databases)) {
        databases = new Databases();
      }
      while (resultSet.next()) {
        databases.add(resultSet.getString("TABLE_CAT"));
      }
    }
  }
}
