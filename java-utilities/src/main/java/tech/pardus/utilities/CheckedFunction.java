/**
 *
 */
package tech.pardus.utilities;

/**
 * @author deniz.toktay
 * @param <T>
 * @param <R>
 * @param <E>
 * @since Aug 20, 2020
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

	/**
	 * @param t
	 * @return <R>
	 * @throws E
	 */
	R apply(T t) throws E;

}
