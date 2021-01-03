/**
 *
 */
package tech.pardus.utilities;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class SessionUserContextHolder {

	private static InheritableThreadLocal<String> sessionContext = new InheritableThreadLocal<>();

	public static void setCurrentSessionUser(String username) {
		sessionContext.set(username);
	}

	public static String getCurrentSessionUser() {
		return sessionContext.get();
	}

	public static void clear() {
		sessionContext.remove();
	}

}
