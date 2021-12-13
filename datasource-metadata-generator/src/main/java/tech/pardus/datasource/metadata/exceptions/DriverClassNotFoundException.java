/**
 *
 */
package tech.pardus.datasource.metadata.exceptions;

/**
 * @author edtoktay
 * @since Nov 24, 2021
 */
public class DriverClassNotFoundException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -3194216661451949350L;

  public DriverClassNotFoundException(String message) {
    super(message);
  }

}
