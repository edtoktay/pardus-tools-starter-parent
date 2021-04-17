package tech.pardus.utilities.test;

/**
 * Exception class for PAsserts Tests
 *
 * @author deniz.toktay
 * @since Aug 19, 2020
 */
public class TestException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;

  /** */
  public TestException() {}

  /** @param message */
  public TestException(String message) {
    super(message);
  }

  /** @param cause */
  public TestException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public TestException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public TestException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
