/**
 *
 */
package tech.pardus.multitenant.datasource.exceptions;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class DatasourceDefinitionException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1713599516298909941L;

	/**
	 *
	 */
	public DatasourceDefinitionException() {
	}

	/**
	 * @param message
	 */
	public DatasourceDefinitionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DatasourceDefinitionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatasourceDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DatasourceDefinitionException(String message, Throwable cause, boolean enableSuppression,
	        boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
