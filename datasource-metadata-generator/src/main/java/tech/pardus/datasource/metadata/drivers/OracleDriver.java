/**
 *
 */
package tech.pardus.datasource.metadata.drivers;

import java.util.Objects;
import tech.pardus.datasource.metadata.DatabaseType;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class OracleDriver implements DbDriver {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private DatabaseProperties properties;
  private JdbcUtils jdbcUtils;

  @Override
  public DatabaseType getDatabaseType() {
    return DatabaseType.ORACLE;
  }

  @Override
  public void setDatabase(DatabaseProperties databaseProperties) {
    this.properties = databaseProperties;
  }

  @Override
  public JdbcUtils getJdbc() {
    if (Objects.isNull(jdbcUtils)) {
      jdbcUtils = new JdbcUtils(properties, getDatabaseUrl());
    }
    return jdbcUtils;
  }

  @Override
  public String getDatabaseUrl() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

}
