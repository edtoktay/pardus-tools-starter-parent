/**
 *
 */
package tech.pardus.multitenant.datasource.entity;

/**
 * @author edtoktay
 *
 */
public class ReadOnlyEntityException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ReadOnlyEntityException() {
	}

	/**
	 * @param message
	 */
	public ReadOnlyEntityException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ReadOnlyEntityException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReadOnlyEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ReadOnlyEntityException(String message, Throwable cause, boolean enableSuppression,
	        boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
