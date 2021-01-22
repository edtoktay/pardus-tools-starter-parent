/** */
package tech.pardus.multitenant.datasource.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import tech.pardus.multitenant.datasource.exceptions.DatasourceDefinitionException;
import tech.pardus.utilities.PAsserts;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Component
@ConfigurationProperties(prefix = "multi-datasource")
public class MultiDataSourceProperties {

  @Getter @Setter private List<DataSourceProperties> datasources = new ArrayList<>();

  @Getter @Setter private String basePackage;

  public DataSourceProperties getPrimaryDataSource() throws Exception {
    PAsserts.notEmpty(
        datasources, () -> "No datasource presented", () -> DatasourceDefinitionException.class);
    return getDatasources()
        .stream()
        .filter(t -> t.isPrimary())
        .findFirst()
        .orElse(getDatasources().get(0));
  }
}
