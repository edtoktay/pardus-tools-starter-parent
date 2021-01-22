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

  boolean interpret(Map<String, ?> bindings);
}
