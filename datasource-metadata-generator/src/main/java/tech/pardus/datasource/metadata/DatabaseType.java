/**
 *
 */
package tech.pardus.datasource.metadata;

import java.util.Arrays;
import lombok.Getter;

/**
 * @author edtoktay
 * @since Nov 24, 2021
 */
public enum DatabaseType {
  // @formatter:off
  ORACLE("oracle", "oracle.jdbc.driver.OracleDriver "),
  POSTGRE("postgresql", "org.postgresql.Driver"),
  MSSQL("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
  MYSQL("mysql", "com.mysql.jdbc.Driver"),
  MARIADB("mariadb", "org.mariadb.jdbc.Driver"),
  H2("h2", "org.h2.Driver"),
  SALESFORCE("salesforce", "com.ddtek.jdbc.sforce.SForceDriver"),
  DB2("db2", "com.ibm.db2.jdbc.net.DB2Driver"),
  AZURE("azuredb", "");
  // @formatter:on

  @Getter
  private String value;
  @Getter
  private String className;

  DatabaseType(String value, String className) {
    this.value = value;
    this.className = className;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static DatabaseType fromValue(String text) {
  // @formatter:off
  return Arrays.stream(DatabaseType.values())
      .filter(t -> String.valueOf(t.value).equals(text))
      .findFirst()
      .orElse(null);
  // @formatter:on
  }

  public static DatabaseType fromClassName(String text) {
    // @formatter:off
    return Arrays.stream(DatabaseType.values())
        .filter(t -> String.valueOf(t.className).equals(text))
        .findFirst()
        .orElse(null);
    // @formatter:on
  }
}
