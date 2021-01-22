/** */
package tech.pardus.multitenant.datasource.processor.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import lombok.extern.slf4j.Slf4j;
import tech.pardus.multitenant.datasource.exceptions.DatasourceAnnotationException;
import tech.pardus.multitenant.datasource.processor.DataSourceConditionConfigurer;
import tech.pardus.multitenant.datasource.processor.DatasourceType;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Slf4j
public class MultiDatasourceCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    try {
      return DatasourceType.MULTIPLE == DataSourceConditionConfigurer.dataSourceType();
    } catch (ClassNotFoundException e) {
      log.error("Multi Datasource Condition", e);
    } catch (DatasourceAnnotationException e) {
      log.error("Multi Datasource Condition", e);
    } catch (Exception e) {
      log.error("Multi Datasource Condition", e);
    }
    return false;
  }
}
