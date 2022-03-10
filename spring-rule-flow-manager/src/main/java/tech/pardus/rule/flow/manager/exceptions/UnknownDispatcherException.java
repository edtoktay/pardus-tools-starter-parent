/**
 * 
 */
package tech.pardus.rule.flow.manager.exceptions;

/**
 * @author deniztoktay
 * @since Mar 10, 2022
 */
public class UnknownDispatcherException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public UnknownDispatcherException() {
    super();
  }

  public UnknownDispatcherException(String message) {
    super(message);
  }

}
