/**
 *
 */
package tech.pardus.rule.flow.manager.models;

import java.io.Serializable;

/**
 * @author deniz.toktay
 * @since Sep 23, 2020
 */
public interface RulePart extends Serializable {

	RulePartType getTypeOfRule();

	default boolean isStatement() {
		return getTypeOfRule() == RulePartType.STATEMENT;
	}

}
