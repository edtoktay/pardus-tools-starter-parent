/**
 *
 */
package tech.pardus.datasource.metadata.drivers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import tech.pardus.datasource.metadata.DatabaseType;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public class DbDriverFactory {

  private static final Map<DatabaseType, Class<? extends DbDriver>> DRIVERS = new HashMap<>();
  static {
    DRIVERS.put(DatabaseType.AZURE, AzureDbDriver.class);
    DRIVERS.put(DatabaseType.DB2, Db2Driver.class);
    DRIVERS.put(DatabaseType.H2, H2Driver.class);
    DRIVERS.put(DatabaseType.MARIADB, MariaDriver.class);
    DRIVERS.put(DatabaseType.MYSQL, MySqlDriver.class);
    DRIVERS.put(DatabaseType.ORACLE, OracleDriver.class);
    DRIVERS.put(DatabaseType.POSTGRE, PostgresDriver.class);
    DRIVERS.put(DatabaseType.SALESFORCE, SalesForceDriver.class);
  }

  public static DbDriver getDriver(DatabaseProperties properties)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    var clazz = DRIVERS.get(properties.getDatabaseType());
    var object = clazz.getConstructor().newInstance();
    object.setDatabase(properties);
    return object;
  }
}
