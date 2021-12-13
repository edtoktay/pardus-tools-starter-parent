/**
 *
 */
package tech.pardus.datasource.metadata.exceptions;

/**
 * @author edtoktay
 * @since Nov 28, 2021
 */
public class DatabaseNotFoundException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1955832424347179290L;

  public DatabaseNotFoundException(String message) {
    super(message);
  }

}
