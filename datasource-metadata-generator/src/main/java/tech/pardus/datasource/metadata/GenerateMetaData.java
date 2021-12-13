/**
 *
 */
package tech.pardus.datasource.metadata;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import tech.pardus.datasource.metadata.drivers.DatabaseProperties;
import tech.pardus.datasource.metadata.drivers.DbDriverFactory;
import tech.pardus.datasource.metadata.drivers.JdbcUtils;
import tech.pardus.datasource.metadata.exceptions.DatabaseNotFoundException;
import tech.pardus.datasource.metadata.models.Databases;
import tech.pardus.datasource.metadata.operations.CatalogMetaDataExtractor;
import tech.pardus.datasource.metadata.operations.SchemaMetaDataExtractor;
import tech.pardus.utilities.PAsserts;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Slf4j
public class GenerateMetaData implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private JdbcUtils jdbcUtils;

  public GenerateMetaData(DatabaseProperties databaseProperties)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    super();
    var dbDriver = DbDriverFactory.getDriver(databaseProperties);
    this.jdbcUtils = dbDriver.getJdbc();
  }

  public Databases getMetaData() {
    var databases = new Databases();
    try (var conn = this.jdbcUtils.getConnection()) {
      var cmde = new CatalogMetaDataExtractor();
      cmde.getAllCatalogs(conn, databases);
    } catch (SQLException e) {
      log.error("Get Catalog Exception", e);
      e.printStackTrace();
    }
    return databases;
  }

  public Databases getAllSchemas() {
    var databases = new Databases();
    try (var conn = this.jdbcUtils.getConnection()) {
      databases = getMetaData();
      getAllSchemas(databases);
    } catch (SQLException e) {
      log.error("Get All Schemas Exception", e);
      e.printStackTrace();
    }
    return databases;
  }

  public void getAllSchemas(Databases databases) {
    PAsserts.notNull(databases, () -> "No Databases found!! Please Check User Priviledges",
        () -> DatabaseNotFoundException.class);
    PAsserts.notEmpty(databases.getDatabases(),
        () -> "No Databases found!! Please Check User Priviledges",
        () -> DatabaseNotFoundException.class);
    databases.getDatabases().stream()
        .forEach(t -> getSchemasOfDatabase(databases, t.getCatalogName()));

  }

  public void getSchemasOfDatabase(Databases databases, String databaseName) {
    try (var conn = this.jdbcUtils.getConnection()) {
      var smde = new SchemaMetaDataExtractor();
      var schemas = smde.getAllSchemas(conn, databaseName);
      databases.getDataBase(databaseName).setSchemas(schemas);
    } catch (SQLException e) {
      log.error("Get Schemas Exception for " + databaseName, e);
      e.printStackTrace();
    }
  }

  public void getSchemasOfDatabases(Databases databases, String... databaseNames) {

  }
}
