package tech.pardus.utilities;

/**
 * Consumer throws exception and terminates the consumer in stream
 *
 * @author deniz.toktay
 * @param <T>
 * @param <E>
 * @since Aug 20, 2020
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Exception> {

  /**
   * @param t
   * @throws E
   */
  void accept(T t) throws E;
}
