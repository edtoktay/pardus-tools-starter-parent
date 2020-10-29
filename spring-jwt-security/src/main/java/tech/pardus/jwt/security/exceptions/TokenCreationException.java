/**
 *
 */
package tech.pardus.jwt.security.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
public class TokenCreationException extends AuthenticationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public TokenCreationException(String msg) {
		super(msg);
	}

}
