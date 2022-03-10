/**
 *
 */
package tech.pardus.spring.utilities;

/**
 * @author dkd10016
 * @since Feb 28, 2020
 */
public class SessionUserContextHolder {

  private static InheritableThreadLocal<String> sessionContext = new InheritableThreadLocal<>();

  /**
   *
   */
  public static void clear() {
    sessionContext.remove();
  }

  /**
   * @return Current Session's user
   */
  public static String getCurrentSessionUser() {
    return sessionContext.get();
  }

  /**
   * Set current session user
   *
   * @param username
   */
  public static void setCurrentSessionUser(String username) {
    sessionContext.set(username);
  }

}
