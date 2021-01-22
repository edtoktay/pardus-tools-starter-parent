/** */
package tech.pardus.multitenant.datasource.exceptions;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
public class DatasourceAnnotationException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;

  /** */
  public DatasourceAnnotationException() {}

  /** @param message */
  public DatasourceAnnotationException(String message) {
    super(message);
  }

  /** @param cause */
  public DatasourceAnnotationException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public DatasourceAnnotationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public DatasourceAnnotationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
