/**
 *
 */
package tech.pardus.jwt.security.properties;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
public interface JwtProperties {

	boolean isIssuer();

	String getSignKey();

	String getHeader();

	String getHeaderPrefix();

}
