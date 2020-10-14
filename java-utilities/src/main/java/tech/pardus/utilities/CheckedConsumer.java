/**
 *
 */
package tech.pardus.utilities;

/**
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Exception> {

	void accept(T t) throws E;

}
