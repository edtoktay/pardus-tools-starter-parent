/**
 *
 */
package tech.pardus.multitenant.datasource.router;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class MultiTenantRouter extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DatasourceRoutingHolder.getCurrentDb();
	}

}
