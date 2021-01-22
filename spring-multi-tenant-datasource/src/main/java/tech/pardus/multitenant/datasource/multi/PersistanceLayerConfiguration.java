package tech.pardus.multitenant.datasource.multi;

import com.zaxxer.hikari.HikariDataSource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tech.pardus.multitenant.datasource.processor.DataSourceConditionConfigurer;
import tech.pardus.multitenant.datasource.processor.conditions.MultiDatasourceCondition;
import tech.pardus.multitenant.datasource.properties.DataSourceProperties;
import tech.pardus.multitenant.datasource.properties.MultiDataSourceProperties;
import tech.pardus.multitenant.datasource.router.MultiTenantRouter;

/**
 * Multiple Datasource configuration class.
 * Activates if only more than one data source define in the application properties file.
 *
 * @author deniz.toktay
 * @since Dec 30, 2020
 */
@Conditional(MultiDatasourceCondition.class)
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableConfigurationProperties(MultiDataSourceProperties.class)
@AutoConfigurationPackage
@ComponentScan("tech.pardus.multitenant.datasource")
public class PersistanceLayerConfiguration {

    @Autowired
    private MultiDataSourceProperties multipleDataSourceProperties;

    /**
     * Configure Multiple Data source.
     *
     * @return DataSource Data Source object for database connection
     * @throws Exception if no DataSource definition found in the application properties file
     */
    @Bean
    public DataSource dataSource() throws Exception {
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        for (var dataSourceId : DataSourceConditionConfigurer.getUsedDataSources()) {
        // @formatter:off
      var property =
          multipleDataSourceProperties
              .getDatasources()
              .stream()
              .filter(t -> t.getId().equalsIgnoreCase(dataSourceId))
              .findFirst()
              .orElseThrow(Exception::new);
      var additionalDataSource =
          DataSourceBuilder.create()
              .type(HikariDataSource.class)
              .url(property.getUrl())
              .username(property.getUsername())
              .password(property.getPassword())
              .driverClassName(property.getDriverClassName())
              .build();
      if (StringUtils.isNotBlank(property.getValidationQuery())) {
        additionalDataSource.setConnectionTestQuery(property.getValidationQuery());
      }
      resolvedDataSources.put(property.getId(), additionalDataSource);
      // @formatter:on
        }
        var dataSource = new MultiTenantRouter();
        dataSource.setDefaultTargetDataSource(defaultDataSource());
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    /**
     * Configure Default (Primary) Data source.
     *
     * @return DataSource Data Source object for database connection
     * @throws Exception if no DataSource definition found in the application properties file
     */
    private DataSource defaultDataSource() throws Exception {
    // @formatter:off
    var properties = getDefaultProperty();
    DataSourceConditionConfigurer.setDefaultDatasource(properties.getId());
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .url(properties.getUrl())
        .username(properties.getUsername())
        .password(properties.getPassword())
        .driverClassName(properties.getDriverClassName())
        .build();
    // @formatter:on
    }

    /**
     * Configure Entity Manager Factory.
     *
     * @param builder entity manager factory builder to configure
     * @return LocalContainerEntityManagerFactoryBean factory bean
     * @throws Exception if no DataSource definition found in the application properties file
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder)
            throws Exception {
    // @formatter:off
    var defaultDatasourceProperty = multipleDataSourceProperties.getPrimaryDataSource();
    var annotatedClass = DataSourceConditionConfigurer.annotatedClass();
    var entityManager =
        builder.dataSource(dataSource()).packages(annotatedClass.getPackageName()).build();
    var vendorAdapter = new HibernateJpaVendorAdapter();
    entityManager.setJpaVendorAdapter(vendorAdapter);
    entityManager
        .getJpaPropertyMap()
        .put("hibernate.dialect", defaultDatasourceProperty.getPlatform());
    entityManager
        .getJpaPropertyMap()
        .put("hibernate.show_sql", defaultDatasourceProperty.isShowSql());
    entityManager
        .getJpaPropertyMap()
        .put("hibernate.format_sql", defaultDatasourceProperty.isFormatSql());
    entityManager
        .getJpaPropertyMap()
        .put("hibernate.hbm2ddl.auto", defaultDatasourceProperty.getHbm2dllAuto());
    entityManager
        .getJpaPropertyMap()
        .put("org.hibernate.envers.audit_table_prefix", defaultDatasourceProperty.getAuditPrefix());
    entityManager
        .getJpaPropertyMap()
        .put("org.hibernate.envers.audit_table_suffix", defaultDatasourceProperty.getAuditSuffix());
    // @formatter:on
        return entityManager;
    }

    /**
     * Find primary data source in configuration file.
     *
     * @return DataSourceProperties primary data source properties
     * @throws Exception if no DataSource definition found in the application properties file
     */
    @Bean
    public DataSourceProperties getDefaultProperty() throws Exception {
        return multipleDataSourceProperties.getPrimaryDataSource();
    }

    /**
     * Configure Transaction Manager
     *
     * @param entityManagerFactory entity manager factory to enable transactions
     * @return PlatformTransactionManager transaction management for datasource
     */
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory")
    EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
