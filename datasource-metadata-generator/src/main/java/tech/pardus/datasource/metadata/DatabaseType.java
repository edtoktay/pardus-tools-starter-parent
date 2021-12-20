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
  ORACLE("Oracle DB", "oracle", "oracle.jdbc.driver.OracleDriver"),
  POSTGRE("PostgreSql", "postgresql", "org.postgresql.Driver"),
  MSSQL("MS SQL", "mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
  MYSQL("MySQL", "mysql", "com.mysql.jdbc.Driver"),
  MARIADB("Maria DB", "mariadb", "org.mariadb.jdbc.Driver"),
  H2("H2", "h2", "org.h2.Driver"),
  SALESFORCE("SALESFORCE DB", "salesforce", "com.ddtek.jdbc.sforce.SForceDriver"),
  DB2("DB2", "db2", "com.ibm.db2.jdbc.net.DB2Driver"),
  AZURE("AZURE DB", "azuredb", "");
  // @formatter:on

  @Getter
  private String name;
  @Getter
  private String value;
  @Getter
  private String className;

  DatabaseType(String name, String value, String className) {
    this.name = name;
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
