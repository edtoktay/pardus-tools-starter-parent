/** */
package tech.pardus.rule.flow.manager.models;

import java.io.Serializable;

/**
 * @author deniz.toktay
 * @since Sep 23, 2020
 */
public interface RulePart extends Serializable {

  /**
   * @return type of the rule
   */
  RulePartType getTypeOfRule();

  /**
   * @return true if the parsed part of the rue is statement
   */
  default boolean isStatement() {
    return getTypeOfRule() == RulePartType.STATEMENT;
  }
}
