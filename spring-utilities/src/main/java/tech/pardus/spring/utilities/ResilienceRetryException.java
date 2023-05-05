package tech.pardus.spring.utilities;

/**
 * @author deniz.toktay
 * @since May 5, 2021
 */
public class ResilienceRetryException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 2853447523960455331L;

	/**
	 *
	 */
	public ResilienceRetryException() {
	}

	/**
	 * @param message
	 */
	public ResilienceRetryException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ResilienceRetryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ResilienceRetryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ResilienceRetryException(String message, Throwable cause, boolean enableSuppression,
	        boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
