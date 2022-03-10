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
 * Asserts functions which throws given runtime exception or runtime exception with given error
 * message or null message
 *
 * @author deniz.toktay
 * @since Aug 19, 2020
 */
public class PAsserts {

  private static final String ASSERT_EXCEPTION_THROWER = "Assert Exception Thrower";
  private static final String UNKNOWN_EXCEPTION = "UnKnown Exception";
  private static final Logger logger = LoggerFactory.getLogger(PAsserts.class);

  /**
   * @param objects
   * @param key
   */
  public static void contains(Collection<Object> objects, Object key) {
    if (objects == null || !containingCheck(objects, key)) {
      throw new AssertException();
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
   * @param container
   * @param contains
   */
  public static void contains(Object container, Object contains) {
    if (container instanceof Collection<?> containerCollection) {
      containingCheck(containerCollection, contains);
    } else if (container instanceof String containerString) {
      contains(containerString, (String) contains);
    } else if (container instanceof Object[] containerArray) {
      containingCheck(containerArray, contains);
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
   * @param text
   * @param key
   */
  public static void contains(String text, String key) {
    if (!StringUtils.contains(text, key)) {
      throw new AssertException();
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
   * @param value
   * @param key
   */
  public static void equals(Object value, Object key) {
    if (value instanceof String stringValue) {
      equals(stringValue, (String) key);
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
    if (value instanceof String stringValue) {
      equals(stringValue, (String) key, message);
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
    if (value instanceof String stringValue) {
      equals(stringValue, (String) key, message, exceptionSuplier);
    } else {
      equalObjects(value, key, message, exceptionSuplier);
    }
  }

  /** @param text */
  public static void hasText(String text) {
    if (StringUtils.isBlank(text)) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void hasText(String text, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (StringUtils.isBlank(text)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param value */
  public static void isFalse(boolean value) {
    if (value) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void isFalse(boolean value, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (value) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param value */
  public static void isFalse(Boolean value) {
    if (value.booleanValue()) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void isFalse(Boolean value, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (value.booleanValue()) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param value */
  public static void isNull(Object value) {
    if (value instanceof String stringValue) {
      noText(stringValue);
    } else if (Objects.nonNull(value)) {
      throw new AssertException();
    }
  }

  /**
   * @param value
   * @param message
   */
  public static void isNull(Object value, Supplier<String> message) {
    if (value instanceof String stringValue) {
      noText(stringValue, message);
    } else if (Objects.nonNull(value)) {
      throw new AssertException(nullSafeGet(message));
    }
  }

  /**
   * @param value
   * @param message
   * @param exceptionSuplier
   */
  public static void isNull(Object value, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (value instanceof String stringValue) {
      noText(stringValue, message, exceptionSuplier);
    } else if (Objects.nonNull(value)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param value */
  public static void isTrue(boolean value) {
    if (!value) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void isTrue(boolean value, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (!value) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param value */
  public static void isTrue(Boolean value) {
    if (!value.booleanValue()) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void isTrue(Boolean value, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (!value.booleanValue()) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param collection */
  public static void notEmpty(Collection<?> collection) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void notEmpty(Collection<?> collection, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (CollectionUtils.isEmpty(collection)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param map */
  public static void notEmpty(Map<?, ?> map) {
    if (MapUtils.isEmpty(map)) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void notEmpty(Map<?, ?> map, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (MapUtils.isEmpty(map)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param array */
  public static void notEmpty(Object[] array) {
    if (ArrayUtils.isEmpty(array)) {
      throw new AssertException();
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

  /** @param text */
  public static void noText(String text) {
    if (StringUtils.isNotEmpty(text)) {
      throw new AssertException();
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
   * @param message
   * @param exceptionSuplier
   */
  public static void noText(String text, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (StringUtils.isNotEmpty(text)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  /** @param value */
  public static void notNull(Object value) {
    if (value instanceof String stringValue) {
      hasText(stringValue);
    } else if (Objects.isNull(value)) {
      throw new AssertException();
    }
  }

  /**
   * @param value
   * @param message
   */
  public static void notNull(Object value, Supplier<String> message) {
    if (value instanceof String stringValue) {
      hasText(stringValue, message);
    } else if (Objects.isNull(value)) {
      throw new AssertException(nullSafeGet(message));
    }
  }

  /**
   * @param value
   * @param message
   * @param exceptionSuplier
   */
  public static void notNull(Object value, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (value instanceof String stringValue) {
      hasText(stringValue, message, exceptionSuplier);
    } else if (Objects.isNull(value)) {
      throw throwException(message, exceptionSuplier);
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

  private static boolean containingCheck(Object[] objects, Object key) {
    var control = false;
    for (var obj : objects) {
      if (obj.equals(key)) {
        control = true;
      }
    }
    return control;
  }

  private static void equalObjects(Object value, Object key) {
    if (!key.equals(value)) {
      throw new AssertException();
    }
  }

  private static void equalObjects(Object value, Object key, Supplier<String> message) {
    if (!key.equals(value)) {
      throw new AssertException(nullSafeGet(message));
    }
  }

  private static void equalObjects(Object value, Object key, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (!key.equals(value)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  private static void equals(String text, String key) {
    if (!StringUtils.equals(text, key)) {
      throw new AssertException();
    }
  }

  private static void equals(String text, String key, Supplier<String> message) {
    if (!StringUtils.equals(text, key)) {
      throw new AssertException(nullSafeGet(message));
    }
  }

  private static void equals(String text, String key, Supplier<String> message,
      Supplier<Class<? extends RuntimeException>> exceptionSuplier) {
    if (!StringUtils.equals(text, key)) {
      throw throwException(message, exceptionSuplier);
    }
  }

  private static void notNull(Class<? extends RuntimeException> exception) {
    if (Objects.isNull(exception)) {
      throw new AssertException(UNKNOWN_EXCEPTION);
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
    try {
      return nullSafeGetException(exceptionSuplier).getConstructor(String.class)
          .newInstance(nullSafeGet(message));
    } catch (InstantiationException | IllegalAccessException | AssertException
        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      logger.error(ASSERT_EXCEPTION_THROWER, e);
    }
    throw new AssertException();
  }

  private PAsserts() {}
}
