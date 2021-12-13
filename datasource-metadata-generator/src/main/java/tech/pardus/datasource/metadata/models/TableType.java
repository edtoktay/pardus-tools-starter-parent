/**
 *
 */
package tech.pardus.datasource.metadata.models;

import java.util.Arrays;

/**
 * @author edtoktay
 * @since Nov 25, 2021
 */
public enum TableType {
  // @formatter:off
  TABLE("TABLE"),
  VIEW("VIEW"),
  SYSTEM_TABLE("SYSTEM TABLE"),
  GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
  LOCAL_TEMPORARY("LOCAL TEMPORARY"),
  ALIAS("ALIAS"),
  SYNONYM("SYNONYM");
  // @formatter:on

  private String value;

  TableType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static TableType fromValue(String text) {
  // @formatter:off
  return Arrays.stream(TableType.values())
      .filter(t -> String.valueOf(t.value).equals(text))
      .findFirst()
      .orElse(null);
  // @formatter:on
  }
}
