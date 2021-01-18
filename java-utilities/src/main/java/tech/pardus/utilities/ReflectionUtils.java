/**
 *
 */
package tech.pardus.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
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
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
public class ReflectionUtils {

	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	private ReflectionUtils() {
	}

	/**
	 * @return main class of the app if there is any
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getMainClass() throws ClassNotFoundException {
		if (Objects.isNull(mainClass)) {
			var threadMap = Thread.getAllStackTraces();
			for (var entry : threadMap.entrySet()) {
				evaluateThreadElements(entry);
			}
		}
		return mainClass;
	}

	/**
	 * @param clazz (Type of target Class) (Type of target Class)
	 * @return initiated class object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object initClass(Class<?> clazz)
	        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return clazz.getConstructor().newInstance();
	}

	/**
	 * @param <T>
	 * @param clazz (Type of target Class)
	 * @return Set of classes which extends given Class
	 */
	public static <T> Set<Class<? extends T>> listOfExtendedClasses(Class<T> clazz) {
		var reflections = new Reflections(findPackageNames());
		return reflections.getSubTypesOf(clazz);
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

	/**
	 * @param clazz      (Type of target Class)
	 * @param methodName
	 * @return Get method of the given method name
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
		return clazz.getMethod(methodName);
	}

	/**
	 * @return Set of Package Names
	 */
	public static Set<String> findPackageNames() {
		// @formatter:off
		Predicate<? super String> notReservedPredicate = t -> isNotReserved(t);
		return Stream
			.of(Package.getPackages())
				.map(Package::getName)
				.filter(notReservedPredicate)
			.collect(Collectors.toSet());
		// @formatter:on
	}

	/**
	 * @param prefix
	 * @return set of package names starting with given prefix
	 */
	public static Set<String> findPackageNamesStartingWith(String prefix) {
		// @formatter:off
		return Stream
				.of(Package.getPackages())
					.map(Package::getName)
					.filter(n -> n.startsWith(prefix))
		        .collect(Collectors.toSet());
		// @formatter:on
	}

	public static Set<Class<?>> getAllClassesAnnotatedWith(Class<? extends Annotation> annotation) {
		var reflections = new Reflections(findPackageNames());
		return reflections.getTypesAnnotatedWith(annotation);
	}

	public static Set<Method> getAllMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		var reflections = new Reflections(findPackageNames());
		return reflections.getMethodsAnnotatedWith(annotation);
	}

	public static Set<Field> getAllFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
		var reflections = new Reflections(findPackageNames());
		return reflections.getFieldsAnnotatedWith(annotation);
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

	private static void evaluateThreadElements(Entry<Thread, StackTraceElement[]> entry) throws ClassNotFoundException {
		for (var element : entry.getValue()) {
			try {
				var stackClass = element.getClassName();
				if (StringUtils.isNotBlank(stackClass) && StringUtils.contains(stackClass, "$")) {
					stackClass = StringUtils.substring(stackClass, 0, StringUtils.lastIndexOf(stackClass, "$"));
				}
				if (isNotReserved(stackClass)) {
					var instance = Class.forName(stackClass);
					var method = instance.getDeclaredMethod("main", String[].class);
					if (Objects.nonNull(method) && Modifier.isStatic(method.getModifiers())) {
						mainClass = instance;
					}
				}
			} catch (NoSuchMethodException e) {
				logger.trace("Searching Main Class");
			}
		}
	}
	// @formatter:off

	private static Set<String> getReservedClasses(){
		if (CollectionUtils.isEmpty(reservedClasses)) {
			reservedClasses = new HashSet<String>();
			try(var bufferedReader = new BufferedReader(new InputStreamReader(ReflectionUtils.class.getClassLoader().getResourceAsStream("excluded_packet_initials")))){
				while (bufferedReader.ready()) {
					var line = bufferedReader.readLine();
					reservedClasses.add(line.strip());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reservedClasses;
	}
	private static Set<String> reservedClasses;
	// @formatter:on

	private static boolean isNotReserved(String name) {
		var control = getReservedClasses().stream().filter(t -> StringUtils.startsWith(name, t)).findFirst()
		        .map(x -> Boolean.FALSE).orElse(Boolean.TRUE);
		return control.booleanValue();
	}

	private static Class<?> mainClass;

}
