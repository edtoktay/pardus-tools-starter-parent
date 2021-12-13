/**
 *
 */
package tech.pardus.datasource.metadata.operations;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import tech.pardus.datasource.metadata.models.Columns;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class ColumnMetaDataExtractor implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Set<Columns> getAllColumns(Connection connection, String tableName) throws SQLException {
    var columns = new HashSet<Columns>();
    var databaseMetaData = connection.getMetaData();
    try (var resultSet = databaseMetaData.getColumns(null, null, tableName, null)) {
      while (resultSet.next()) {
        // @formatter:off
        columns.add(
            Columns
              .builder()
                .columnName(resultSet.getString("COLUMN_NAME"))
                .dataType(null)
                .dataTypeName(resultSet.getString("TYPE_NAME"))
                .columnSize(resultSet.getInt("COLUMN_SIZE"))
                .decimalDigits(resultSet.getInt("DECIMAL_DIGITS"))
                .nullable(resultSet.getString("IS_NULLABLE").equalsIgnoreCase("YES") ? true : false)
                .remarks(resultSet.getString("REMARKS"))
                .columnDefault(resultSet.getString("COLUMN_DEF"))
                .ordinalPosition(resultSet.getInt("ORDINAL_POSITION"))
                .autoIncrement(resultSet.getString("IS_AUTOINCREMENT").equalsIgnoreCase("YES") ? true : false)
                .relations(null)
                .primaryKey(false)
              .build()
        );
        // @formatter:on
      }
    }
    return columns;
  }

}
