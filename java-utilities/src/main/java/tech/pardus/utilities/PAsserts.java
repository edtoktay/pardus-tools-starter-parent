/**
 *
 */
package tech.pardus.utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author .toktay
 * @since Aug 19, 2020
 */
public class PAsserts {

	private static final Logger logger = LoggerFactory.getLogger(PAsserts.class);

	private PAsserts() {
	}

	/**
	 * @param text
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void hasText(String text, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (StringUtils.isBlank(text)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param text
	 * @param message
	 */
	public static void hasText(String text, Supplier<String> message) {
		if (StringUtils.isBlank(text)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param text
	 */
	public static void hasText(String text) {
		if (StringUtils.isBlank(text)) {
			throw new AssertException();
		}
	}

	/**
	 * @param text
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void noText(String text, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (StringUtils.isNotEmpty(text)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param text
	 * @param message
	 */
	public static void noText(String text, Supplier<String> message) {
		if (StringUtils.isNotEmpty(text)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param text
	 */
	public static void noText(String text) {
		if (StringUtils.isNotEmpty(text)) {
			throw new AssertException();
		}
	}

	/**
	 * @param array
	 */
	public static void notEmpty(Object[] array) {
		if (ArrayUtils.isEmpty(array)) {
			throw new AssertException();
		}
	}

	/**
	 * @param array
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void notEmpty(Object[] array, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (ArrayUtils.isEmpty(array)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param array
	 * @param message
	 */
	public static void notEmpty(Object[] array, Supplier<String> message) {
		if (ArrayUtils.isEmpty(array)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param collection
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void notEmpty(Collection<?> collection, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (CollectionUtils.isEmpty(collection)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param collection
	 * @param message
	 */
	public static void notEmpty(Collection<?> collection, Supplier<String> message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param collection
	 */
	public static void notEmpty(Collection<?> collection) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new AssertException();
		}
	}

	/**
	 * @param map
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void notEmpty(Map<?, ?> map, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (MapUtils.isEmpty(map)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param map
	 * @param message
	 */
	public static void notEmpty(Map<?, ?> map, Supplier<String> message) {
		if (MapUtils.isEmpty(map)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param map
	 */
	public static void notEmpty(Map<?, ?> map) {
		if (MapUtils.isEmpty(map)) {
			throw new AssertException();
		}
	}

	private static String nullSafeGet(Supplier<String> messageSupplier) {
		return messageSupplier != null ? messageSupplier.get() : null;
	}

	private static Class<? extends RuntimeException> nullSafeGetException(
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		var exception = exceptionSuplier != null ? exceptionSuplier.get() : null;
		notNull(exception);
		return exception;
	}

	private static RuntimeException throwException(Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		var msg = "Assert Exception Thrower";
		try {
			return nullSafeGetException(exceptionSuplier).getConstructor(String.class)
			        .newInstance(nullSafeGet(message));
		} catch (InstantiationException | IllegalAccessException | AssertException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			logger.error(msg, e);
		}
		throw new AssertException();
	}

	private static void notNull(Class<? extends RuntimeException> exception) {
		if (Objects.isNull(exception)) {
			throw new AssertException("UnKnown Exception");
		}
	}

	/**
	 * @param text
	 * @param key
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void contains(String text, String key, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (!StringUtils.contains(text, key)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param text
	 * @param key
	 * @param message
	 */
	public static void contains(String text, String key, Supplier<String> message) {
		if (!StringUtils.contains(text, key)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param text
	 * @param key
	 */
	public static void contains(String text, String key) {
		if (!StringUtils.contains(text, key)) {
			throw new AssertException();
		}
	}

	/**
	 * @param objects
	 * @param key
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void contains(Object[] objects, Object key, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (objects == null || !containingCheck(objects, key)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param objects
	 * @param key
	 * @param message
	 */
	public static void contains(Object[] objects, Object key, Supplier<String> message) {
		if (objects == null || !containingCheck(objects, key)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param objects
	 * @param key
	 */
	public static void contains(Object[] objects, Object key) {
		if (objects == null || !containingCheck(objects, key)) {
			throw new AssertException();
		}
	}

	private static boolean containingCheck(Object[] objects, Object key) {
		var control = false;
		for (var obj : objects) {
			if (obj.equals(key)) {
				control = true;
			}
		}
		return control;
	}

	/**
	 * @param objects
	 * @param key
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void contains(Collection<Object> objects, Object key, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (objects == null || !containingCheck(objects, key)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param objects
	 * @param key
	 * @param message
	 */
	public static void contains(Collection<Object> objects, Object key, Supplier<String> message) {
		if (objects == null || !containingCheck(objects, key)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param objects
	 * @param key
	 */
	public static void contains(Collection<Object> objects, Object key) {
		if (objects == null || !containingCheck(objects, key)) {
			throw new AssertException();
		}
	}

	private static boolean containingCheck(Collection<?> objects, Object key) {
		var control = false;
		for (var obj : objects) {
			if (obj.equals(key)) {
				control = true;
			}
		}
		return control;
	}

	/**
	 * @param container
	 * @param contains
	 */
	public static void contains(Object container, Object contains) {
		if (container instanceof Collection<?>) {
			containingCheck((Collection<?>) container, contains);
		} else if (container instanceof String) {
			contains((String) container, (String) contains);
		} else if (container instanceof Object[]) {
			containingCheck((Object[]) container, contains);
		}
	}

	private static void equals(String text, String key, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (!StringUtils.equals(text, key)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	private static void equals(String text, String key, Supplier<String> message) {
		if (!StringUtils.equals(text, key)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	private static void equals(String text, String key) {
		if (!StringUtils.equals(text, key)) {
			throw new AssertException();
		}
	}

	private static void equalObjects(Object value, Object key, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (!key.equals(value)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	private static void equalObjects(Object value, Object key, Supplier<String> message) {
		if (!key.equals(value)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	private static void equalObjects(Object value, Object key) {
		if (!key.equals(value)) {
			throw new AssertException();
		}
	}

	/**
	 * @param value
	 * @param key
	 */
	public static void equals(Object value, Object key) {
		if (value instanceof String) {
			equals((String) value, (String) key);
		} else {
			equalObjects(value, key);
		}
	}

	/**
	 * @param value
	 * @param key
	 * @param message
	 */
	public static void equals(Object value, Object key, Supplier<String> message) {
		if (value instanceof String) {
			equals((String) value, (String) key, message);
		} else {
			equalObjects(value, key, message);
		}
	}

	/**
	 * @param value
	 * @param key
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void equals(Object value, Object key, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (value instanceof String) {
			equals((String) value, (String) key, message, exceptionSuplier);
		} else {
			equalObjects(value, key, message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void isTrue(boolean value, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (!value) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 */
	public static void isTrue(boolean value, Supplier<String> message) {
		if (!value) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param value
	 */
	public static void isTrue(boolean value) {
		if (!value) {
			throw new AssertException();
		}
	}

	/**
	 * @param value
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void isTrue(Boolean value, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (!value.booleanValue()) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 */
	public static void isTrue(Boolean value, Supplier<String> message) {
		if (!value.booleanValue()) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param value
	 */
	public static void isTrue(Boolean value) {
		if (!value.booleanValue()) {
			throw new AssertException();
		}
	}

	/**
	 * @param value
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void isFalse(boolean value, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (value) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 */
	public static void isFalse(boolean value, Supplier<String> message) {
		if (value) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param value
	 */
	public static void isFalse(boolean value) {
		if (value) {
			throw new AssertException();
		}
	}

	/**
	 * @param value
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void isFalse(Boolean value, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (value.booleanValue()) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 */
	public static void isFalse(Boolean value, Supplier<String> message) {
		if (value.booleanValue()) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param value
	 */
	public static void isFalse(Boolean value) {
		if (value.booleanValue()) {
			throw new AssertException();
		}
	}

	/**
	 * @param value
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void notNull(Object value, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (value instanceof String) {
			hasText((String) value, message, exceptionSuplier);
		} else if (Objects.isNull(value)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 */
	public static void notNull(Object value, Supplier<String> message) {
		if (value instanceof String) {
			hasText((String) value, message);
		} else if (Objects.isNull(value)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param value
	 */
	public static void notNull(Object value) {
		if (value instanceof String) {
			hasText((String) value);
		} else if (Objects.isNull(value)) {
			throw new AssertException();
		}
	}

	/**
	 * @param value
	 * @param message
	 * @param exceptionSuplier
	 */
	public static void isNull(Object value, Supplier<String> message,
	        Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
		if (value instanceof String) {
			noText((String) value, message, exceptionSuplier);
		} else if (Objects.nonNull(value)) {
			throw throwException(message, exceptionSuplier);
		}
	}

	/**
	 * @param value
	 * @param message
	 */
	public static void isNull(Object value, Supplier<String> message) {
		if (value instanceof String) {
			noText((String) value, message);
		} else if (Objects.nonNull(value)) {
			throw new AssertException(nullSafeGet(message));
		}
	}

	/**
	 * @param value
	 */
	public static void isNull(Object value) {
		if (value instanceof String) {
			noText((String) value);
		} else if (Objects.nonNull(value)) {
			throw new AssertException();
		}
	}

}
