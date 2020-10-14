/**
 *
 */
package tech.pardus.utilities;

/**
 * @author deniz.toktay
 * @since Aug 19, 2020
 */
public class AssertException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3688580823601372463L;

	/**
	 *
	 */
	public AssertException() {
	}

	/**
	 * @param message
	 */
	public AssertException(String message) {
		super(message);
	}

}
