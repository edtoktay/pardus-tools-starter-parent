/**
 *
 */
package tech.pardus.datasource.metadata.drivers;

import java.io.Serializable;
import tech.pardus.datasource.metadata.DatabaseType;

/**
 * @author edtoktay
 * @since Nov 24, 2021
 */
public interface DbDriver extends Serializable, AutoCloseable {

  DatabaseType getDatabaseType();

  void setDatabase(DatabaseProperties databaseProperties);

  JdbcUtils getJdbc();

  String getDatabaseUrl();

}
