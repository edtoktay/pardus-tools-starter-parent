/** */
package tech.pardus.multitenant.datasource.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import tech.pardus.multitenant.datasource.DataSource;
import tech.pardus.multitenant.datasource.DataSourceSelection;
import tech.pardus.multitenant.datasource.EnableMultiTenancy;
import tech.pardus.multitenant.datasource.exceptions.DatasourceAnnotationException;
import tech.pardus.utilities.PAsserts;
import tech.pardus.utilities.ReflectionUtils;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class DataSourceConditionConfigurer {

  private static HashMap<String, String> entityDataSourceMap = new HashMap<>();

  @Setter private static String defaultDatasource;

  @Getter private static Set<String> usedDataSources = new HashSet<>();

  public static DatasourceType dataSourceType()
      throws DatasourceAnnotationException, ClassNotFoundException {
    var annotation = getAnnotation();
    if (annotation.enable()) {
      PAsserts.notEmpty(
          annotation.datasources(),
          () -> "Multiple Datasource Enabled, but, no datasource presented",
          () -> DatasourceAnnotationException.class);
      usedDataSources =
          Stream.of(annotation.datasources()).map(DataSource::id).collect(Collectors.toSet());
      return DatasourceType.MULTIPLE;
    }
    return DatasourceType.SINGLE;
  }

  public static void handleEntityDatasourceAggregation(EntityManagerFactory entityManagerFactory) {
    var entityTypes = entityManagerFactory.getMetamodel().getEntities();
    for (var entity : entityTypes) {
      var javaType = entity.getJavaType();
      var tableAnnotation = javaType.getAnnotation(Table.class);
      var sb = new StringBuilder();
      if (Objects.nonNull(tableAnnotation)) {
        if (StringUtils.isNotBlank(tableAnnotation.schema())) {
          sb.append(tableAnnotation.schema()).append(".");
        }
        if (StringUtils.isNotBlank(tableAnnotation.name())) {
          sb.append(tableAnnotation.name());
        } else {
          sb.append(javaType.getSimpleName());
        }
      } else {
        sb.append(javaType.getSimpleName());
      }
      String datasource = defaultDatasource;
      var dataSourceAnntotation = javaType.getAnnotation(DataSourceSelection.class);
      if (Objects.nonNull(dataSourceAnntotation)) {
        datasource = dataSourceAnntotation.datasource();
      }
      entityDataSourceMap.put(sb.toString(), datasource);
    }
  }

  public static Class<?> annotatedClass() {
    var annotatedClasses = ReflectionUtils.getAllClassesAnnotatedWith(EnableMultiTenancy.class);
    PAsserts.notEmpty(
        annotatedClasses,
        () -> "Missing EnableMultiTenancy Annotation",
        () -> DatasourceAnnotationException.class);
    PAsserts.isTrue(
        annotatedClasses.size() == 1,
        () -> "Multiple EnableMultiTenancy Annotations found",
        () -> DatasourceAnnotationException.class);
    var iterator = annotatedClasses.iterator();
    return iterator.next();
  }

  private static EnableMultiTenancy getAnnotation() {
    var definedBean = annotatedClass();
    return definedBean.getAnnotation(EnableMultiTenancy.class);
  }

  public static String getDataSourceForTable(String table) {
    return entityDataSourceMap.containsKey(table)
        ? entityDataSourceMap.get(table)
        : defaultDatasource;
  }
}
