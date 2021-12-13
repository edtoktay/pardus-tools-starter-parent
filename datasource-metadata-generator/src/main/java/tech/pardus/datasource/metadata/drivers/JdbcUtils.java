/**
 *
 */
package tech.pardus.datasource.metadata.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.Getter;
import lombok.Setter;
import tech.pardus.datasource.metadata.exceptions.DriverClassNotFoundException;
import tech.pardus.utilities.PAsserts;

/**
 * @author edtoktay
 * @since Nov 24, 2021
 */
@Getter
@Setter
public final class JdbcUtils {
  private DatabaseProperties properties;
  private String databaseUrl;

  public JdbcUtils(DatabaseProperties properties, String databaseUrl) {
    super();
    this.properties = properties;
    this.databaseUrl = databaseUrl;
  }

  public Connection getConnection() throws SQLException {
    checkIfDriverClassExists();
    var conn = DriverManager.getConnection(databaseUrl, properties.getUserName(),
        properties.getPassword());
    return conn;
  }

  private void checkIfDriverClassExists() {
    var classExists = true;
    try {
      Class.forName(properties.getDatabaseType().getClassName());
    } catch (ClassNotFoundException e) {
      classExists = false;
    }
    PAsserts.isTrue(classExists, () -> "Driver Class Not Found",
        () -> DriverClassNotFoundException.class);
  }

}
