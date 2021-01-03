/**
 *
 */
package tech.pardus.multitenant.datasource.single;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
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

import com.zaxxer.hikari.HikariDataSource;

import tech.pardus.multitenant.datasource.processor.DataSourceConditionConfigurer;
import tech.pardus.multitenant.datasource.processor.conditions.SingleDatasourceCondition;
import tech.pardus.multitenant.datasource.properties.MultiDataSourceProperties;

/**
 * @author deniz.toktay
 * @since Dec 30, 2020
 */
@Conditional(SingleDatasourceCondition.class)
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
// @EnableConfigurationProperties(MultiDataSourceProperties.class)
@AutoConfigurationPackage
@ComponentScan("tech.pardus.multitenant.datasource")
public class PersistanceLayerConfiguration {

	@Autowired
	private MultiDataSourceProperties multipleDataSourceProperties;

	@Bean
	@Primary
	public DataSource dataSource() throws Exception {
		// @formatter:off
		var properties = multipleDataSourceProperties.getPrimaryDataSource();
		return DataSourceBuilder
					.create()
						.type(HikariDataSource.class)
						.url(properties.getUrl())
						.username(properties.getUsername())
						.password(properties.getPassword())
						.driverClassName(properties.getDriverClassName())
					.build();
		// @formatter:on
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder)
	        throws Exception {
		// @formatter:off
		var defaultDatasourceProperty = multipleDataSourceProperties.getPrimaryDataSource();
		var annotatedClass = DataSourceConditionConfigurer.annotatedClass();
        var entityManager =  builder
			        			.dataSource(dataSource())
			        			.packages(annotatedClass.getPackageName())
			        		.build();
        var vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);
        entityManager.getJpaPropertyMap().put("hibernate.dialect", defaultDatasourceProperty.getPlatform());
        entityManager.getJpaPropertyMap().put("hibernate.show_sql", defaultDatasourceProperty.isShowSql());
        entityManager.getJpaPropertyMap().put("hibernate.format_sql", defaultDatasourceProperty.isFormatSql());
        entityManager.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", defaultDatasourceProperty.getHbm2dllAuto());
        entityManager.getJpaPropertyMap().put("org.hibernate.envers.audit_table_prefix",
				defaultDatasourceProperty.getAuditPrefix());
        entityManager.getJpaPropertyMap().put("org.hibernate.envers.audit_table_suffix",
				defaultDatasourceProperty.getAuditSuffix());
        // @formatter:on
		return entityManager;
	}

	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(
	        @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
