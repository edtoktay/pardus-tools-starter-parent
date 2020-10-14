/**
 *
 */
package tech.pardus.utilities;

/**
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

	R apply(T t) throws E;

}
