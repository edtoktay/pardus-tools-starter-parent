/**
 *
 */
package tech.pardus.multitenant.datasource.properties;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Getter
@Setter
@Component
public class DataSourceProperties {

	private String id;

	private String url;

	private String username;

	private String password;

	private String platform;

	private String driverClassName;

	private boolean showSql = false;

	private boolean formatSql = false;

	private String hbm2dllAuto = "none";

	private boolean primary = false;

	private String auditPrefix = "";

	private String auditSuffix = "_AUD";

	private String validationQuery = "";

}
