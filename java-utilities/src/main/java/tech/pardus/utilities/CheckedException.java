package tech.pardus.utilities;

/**
 * Exception to throw for Checked Functions and Consumers
 *
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
public class CheckedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /** @param cause */
  public CheckedException(Throwable cause) {
    super(cause);
  }
}
