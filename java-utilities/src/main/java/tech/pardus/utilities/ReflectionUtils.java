package tech.pardus.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reflection operations to handle class and annotation search operations and other reflective
 * operations
 *
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
public class ReflectionUtils {

  private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

  private ReflectionUtils() {}

  /**
   * @param prefix
   * @return set of package names starting with given prefix
   */
  public static Set<String> findPackageNamesStartingWith(String prefix) {
    // @formatter:off
    return Stream.of(Package.getPackages())
        .map(Package::getName)
        .filter(n -> n.startsWith(prefix))
        .collect(Collectors.toSet());
    // @formatter:on
  }

  /**
   * @param clazz (Type of target Class)
   * @param methodName
   * @return Get method of the given method name
   * @throws NoSuchMethodException
   */
  public static Method getMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
    return clazz.getMethod(methodName);
  }

  /**
   * @param clazz (Type of target Class) (Type of target Class)
   * @return initiated class object
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  public static Object initClass(Class<?> clazz) throws InstantiationException,
      IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    return clazz.getConstructor().newInstance();
  }

  /**
   * @param method
   * @return true if method is static
   */
  public static boolean isMethodStatic(Method method) {
    return Modifier.isStatic(method.getModifiers());
  }

  /**
   * @param object
   * @param methodName
   * @param args
   * @return method return after execution
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  public static Object runMethod(Object object, String methodName, Object... args)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Object result = null;
    var method = getMethod(object.getClass(), methodName);
    if (isMethodStatic(method)) {
      result = runStaticMethod(object.getClass(), methodName, args);
    } else {
      result = runJavaMethod(object, methodName, args);
    }
    return result;
  }

  private static Object runJavaMethod(Object object, String methodName, Object... args)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Object result = null;
    var method = getMethod(object.getClass(), methodName);
    result = method.invoke(object, args);
    return result;
  }

  private static Object runStaticMethod(Class<?> clazz, String methodName, Object... args)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    var method = getMethod(clazz, methodName);
    return method.invoke(null, args);
  }
  // @formatter:on

  /**
   * @param <T>
   * @param clazz (Type of target Class)
   * @return Set of classes which extends given Class
   */
  public static <T> Set<Class<? extends T>> listOfExtendedClasses(Class<T> clazz) {
    var reflections = new Reflections(findPackageNames());
    return reflections.getSubTypesOf(clazz);
  }

  /** @return Set of Package Names */
  public static Set<String> findPackageNames() {
    // @formatter:off
    Predicate<? super String> notReservedPredicate = ReflectionUtils::isNotReserved;
    return Stream.of(Package.getPackages())
        .map(Package::getName)
        .filter(notReservedPredicate)
        .collect(Collectors.toSet());
    // @formatter:on
  }

  private static boolean isNotReserved(String name) {
    var control = getReservedClasses().stream().filter(t -> StringUtils.startsWith(name, t))
        .findFirst().map(x -> Boolean.FALSE).orElse(Boolean.TRUE);
    return control.booleanValue();
  }

  private static Set<String> getReservedClasses() {
    if (CollectionUtils.isEmpty(reservedClasses)) {
      reservedClasses = new HashSet<>();
      try (var bufferedReader = new BufferedReader(new InputStreamReader(ReflectionUtils.class
          .getClassLoader().getResourceAsStream("excluded_packet_initials")))) {
        while (bufferedReader.ready()) {
          var line = bufferedReader.readLine();
          reservedClasses.add(line.strip());
        }
      } catch (IOException e) {
        logger.error("reserved class exception", e);
      }
    }
    return reservedClasses;
  }

  private static Set<String> reservedClasses;
}
