/**
 *
 */
package tech.pardus.multitenant.datasource.router;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class DatasourceRoutingHolder {

	private static InheritableThreadLocal<String> dbContext = new InheritableThreadLocal<>();

	public static void setCurrentDb(String dbType) {
		dbContext.set(dbType);
	}

	public static String getCurrentDb() {
		return dbContext.get();
	}

	public static void clear() {
		dbContext.remove();
	}

}
