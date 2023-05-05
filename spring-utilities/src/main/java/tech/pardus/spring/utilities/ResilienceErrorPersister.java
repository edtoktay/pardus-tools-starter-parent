package tech.pardus.spring.utilities;

/**
 * @author deniz.toktay
 * @since May 5, 2021
 */
public interface ResilienceErrorPersister {

	void persistError(Object... args);

}
