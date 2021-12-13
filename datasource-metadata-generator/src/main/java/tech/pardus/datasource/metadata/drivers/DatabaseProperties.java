/**
 *
 */
package tech.pardus.datasource.metadata.drivers;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.pardus.datasource.metadata.DatabaseType;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatabaseProperties implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5654116480936282010L;

  private String url;
  private String dbName;
  private int port;
  private String userName;
  private String password;
  private String secretToken;
  private DatabaseType databaseType;

}
