/**
 *
 */
package tech.pardus.datasource.metadata;

import static org.junit.Assert.assertNotNull;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tech.pardus.datasource.metadata.drivers.DatabaseProperties;
import tech.pardus.datasource.metadata.drivers.DbDriverFactory;
import tech.pardus.datasource.metadata.models.Databases;
import tech.pardus.datasource.metadata.operations.CatalogMetaDataExtractor;
import tech.pardus.datasource.metadata.operations.ColumnMetaDataExtractor;
import tech.pardus.datasource.metadata.operations.SchemaMetaDataExtractor;
import tech.pardus.datasource.metadata.operations.TableMetaDataExtractor;

/**
 * @author edtoktay
 * @since 21 Nov, 2021
 */
@Testcontainers
class MetaDataExtractorPostgres {

  private static final String POSTGRES_VERSION = "postgres:12";

  private DatabaseProperties props;
  @Container
  public PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_VERSION))
          .withInitScript("databases/postgres/init-postgres.sql");

  @BeforeEach
  void setUp() {
  //@formatter:off
    props = DatabaseProperties
                  .builder()
                    .url(postgres.getHost())
                    .port(postgres.getFirstMappedPort())
                    .dbName(postgres.getDatabaseName())
                    .userName(postgres.getUsername())
                    .password(postgres.getPassword())
                    .databaseType(DatabaseType.POSTGRE)
                  .build();
    // @formatter:on
  }

  @Test
  void test() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    var dbDriver = DbDriverFactory.getDriver(props);
    var jdbcUtil = dbDriver.getJdbc();
    var url = postgres.getJdbcUrl();
    try (var conn = jdbcUtil.getConnection()) {
      var databases = new Databases();
      var cmde = new CatalogMetaDataExtractor();
      cmde.getAllCatalogs(conn, databases);
      var smde = new SchemaMetaDataExtractor();
      var hede = smde.getAllSchemas(conn, "test");
      System.out.println(hede);
      hede.stream().map(t -> t.getSchemaName()).forEach(System.out::println);
      var mde = new TableMetaDataExtractor();
      mde.getAllTables(conn, "test", "sales");
      var cdw = new ColumnMetaDataExtractor();
      cdw.getAllColumns(conn, "product_oder_view");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println(url);
  }

  @Test
  void test_catalogs()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    var gmd = new GenerateMetaData(props);
    var databases = gmd.getMetaData();
    assertNotNull(databases);
  }

  @Test
  void test_all_schemas()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    var gmd = new GenerateMetaData(props);
    var databases = gmd.getAllSchemas();
    assertNotNull(databases);
  }
}
