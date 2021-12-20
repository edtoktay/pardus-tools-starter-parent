/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.util.Set;

/**
 * @author edtoktay
 * @since Dec 14, 2021
 */
public class MetaDataConstants {
  // @formatter:off
  public static final Set<String> reservedSchemas =
      Set.of(
          // Postges reserved Schemas
          "information_schema",
          "pg_catalog",
          "pg_toast");
  // @formatter:on
}
