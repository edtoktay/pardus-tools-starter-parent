/**
 *
 */
package tech.pardus.utilities;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
public class LambdaWrapper {

	private LambdaWrapper() {
	}

	public static <T, R> Function<T, R> functionChecker(CheckedFunction<T, R, Exception> checkedFunction) {
		return t -> {
			try {
				return checkedFunction.apply(t);
			} catch (Exception e) {
				throw new CheckedException(e);
			}
		};
	}

	public static <T> Consumer<T> consumerChecker(CheckedConsumer<T, Exception> throwingConsumer) {
		return i -> {
			try {
				throwingConsumer.accept(i);
			} catch (Exception ex) {
				throw new CheckedException(ex);
			}
		};
	}

}
