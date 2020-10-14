/**
 *
 */
package tech.pardus.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	public static Class<?> getMainClass() throws ClassNotFoundException {
		if (Objects.isNull(mainClass)) {
			var threadMap = Thread.getAllStackTraces();
			for (var entry : threadMap.entrySet()) {
				evaluateThreadElements(entry);
			}
		}
		return mainClass;
	}

	public static Object initClass(Class<?> clazz)
	        throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return clazz.getConstructor().newInstance();
	}

	public static <T> Set<Class<? extends T>> listOfExtendedClasses(Class<T> clazz) {
		var reflections = new Reflections(findPackageNames());
		return reflections.getSubTypesOf(clazz);
	}

	public static boolean isMethodStatic(Method method) {
		return Modifier.isStatic(method.getModifiers());
	}

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

	public static Method getMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
		return clazz.getMethod(methodName);
	}

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

	public static Set<String> findPackageNamesStartingWith(String prefix) {
		// @formatter:off
		return Stream
				.of(Package.getPackages())
					.map(Package::getName)
					.filter(n -> n.startsWith(prefix))
		        .collect(Collectors.toSet());
		// @formatter:on
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
	private static Set<String> reservedClasses = new HashSet<>(
	        Arrays.asList("java.", "jdk.", "sun.",
	        		"com.sun.", "org.spring", "org.eclipse",
	        		"org.junit", "org.apache", "org.slf4j",
	        		"org.opentest4j", "org.apiguardian",
	        		"lombok", "com.hazelcast", "org.aspectj",
	        		"org.hibernate"));
	// @formatter:on

	private static boolean isNotReserved(String name) {
		var control = reservedClasses.stream().filter(t -> StringUtils.startsWith(name, t)).findFirst()
		        .map(x -> Boolean.FALSE).orElse(Boolean.TRUE);
		return control.booleanValue();
	}

	private static Class<?> mainClass;

}
