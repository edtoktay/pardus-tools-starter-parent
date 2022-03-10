/** */
package tech.pardus.rule.flow.manager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.pardus.rule.flow.manager.expressions.Expression;
import tech.pardus.utilities.PAsserts;

/**
 * @author deniz.toktay
 * @since Sep 10, 2020
 */
@Getter
@Setter
@NoArgsConstructor
public class ExpressionModel implements RulePart {

  /** */
  private static final long serialVersionUID = 1L;

  private Expression expression;

  private boolean elseExpression = false;

  private boolean headExpression = false;

  /**
   * @return ExpressionBuilder
   */
  public static ExpressionBuilder expression() {
    return new ExpressionModel.ExpressionBuilder();
  }

  /**
   * @author deniz.toktay
   * @since Sep 10, 2020
   */
  @NoArgsConstructor
  public static class ExpressionBuilder {

    private final ExpressionModel managedInstance = new ExpressionModel();

    /**
     * @param expr
     * @return ExpressionBuilder
     */
    public ExpressionBuilder expression(Expression expr) {
      PAsserts.isFalse(managedInstance.elseExpression, () -> "already_defined_else");
      this.managedInstance.expression = expr;
      return this;
    }

    /**
     * @return ExpressionBuilder
     */
    public ExpressionBuilder elseExpression() {
      PAsserts.isNull(managedInstance.expression, () -> "already_defined_rule");
      managedInstance.elseExpression = true;
      return this;
    }

    /**
     * @return ExpressionBuilder
     */
    public ExpressionBuilder isHeadExpression() {
      managedInstance.headExpression = true;
      return this;
    }

    /**
     * @return ExpressionModel
     */
    public ExpressionModel addExpression() {
      return managedInstance;
    }
  }

  @Override
  public RulePartType getTypeOfRule() {
    return RulePartType.STATEMENT;
  }
}
