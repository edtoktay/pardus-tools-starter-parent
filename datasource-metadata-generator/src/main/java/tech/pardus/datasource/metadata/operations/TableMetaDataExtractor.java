/**
 *
 */
package tech.pardus.datasource.metadata.operations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import tech.pardus.datasource.metadata.models.TableType;
import tech.pardus.datasource.metadata.models.Tables;

/**
 * @author edtoktay
 * @since 21 Nov, 2021
 */
public class TableMetaDataExtractor implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -6696433171172185172L;

  public Set<Tables> getAllTables(Connection connection) throws SQLException {
    return getTables(connection, null, null, TableType.values());
  }

  public Set<Tables> getAllTables(Connection connection, String catalog, String schema)
      throws SQLException {
    return getTables(connection, catalog, schema, TableType.values());
  }

  public Set<Tables> getUserTables(Connection connection, String catalog, String schema)
      throws SQLException {
    return getTables(connection, catalog, schema,
        new TableType[] {TableType.TABLE, TableType.VIEW});
  }

  private Set<Tables> getTables(Connection connection, String catalog, String schema,
      TableType... tableTypes) throws SQLException {
    var tables = new HashSet<Tables>();
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getTables(catalog, schema, null,
        Arrays.stream(tableTypes).map(TableType::toString).toArray(String[]::new));) {
      while (resultSet.next()) {
        // @formatter:off
        tables.add(
            Tables
              .builder()
                .tableName(resultSet.getString("TABLE_NAME"))
                .tableType(TableType.fromValue(resultSet.getString("TABLE_TYPE")))
                .remarks(resultSet.getString("REMARKS"))
                .columns(null)
                .primaryKeys(null)
              .build()
        );
        // @formatter:on
      }
    }
    return tables;
  }
}
