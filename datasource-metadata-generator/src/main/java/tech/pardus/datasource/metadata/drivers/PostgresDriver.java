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
public class PostgresDriver implements DbDriver {

  /**
   *
   */
  private static final long serialVersionUID = -1763208623885185456L;

  private static final String JDBC_URL = "jdbc:";

  private DatabaseProperties properties;
  private JdbcUtils jdbcUtils;

  @Override
  public DatabaseType getDatabaseType() {
    return DatabaseType.POSTGRE;
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
    var URL = new StringBuffer();
    URL.append(JDBC_URL).append(properties.getDatabaseType().toString()).append("://")
        .append(properties.getUrl()).append(":").append(properties.getPort()).append("/")
        .append(properties.getDbName());
    return URL.toString();
  }

}
