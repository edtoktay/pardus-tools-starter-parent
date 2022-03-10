/** */
package tech.pardus.rule.flow.manager.expressions;

import java.io.Serializable;
import java.util.Map;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
@FunctionalInterface
public interface Expression extends Serializable {

  /**
   * Run any expression based on given values
   * 
   * @param bindings Map of bindings first String element contains args given in the rule statement
   *        second is the object to use by processor
   * @return result of the expression by using given values
   */
  boolean interpret(Map<String, ?> bindings);
}
