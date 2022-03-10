/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.Deque;
import java.util.Map;
import tech.pardus.rule.flow.manager.expressions.Expression;

/**
 * Predefined Or (defined with OR symbol) operation || in given objects preferred to use by boolean
 * values
 * 
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public class Or extends Operation {

  /** */
  private static final long serialVersionUID = -89821281802173244L;

  /**
   * 
   */
  public Or() {
    super("OR");
  }

  @Override
  public boolean interpret(Map<String, ?> bindings) {
    return leftOperand.interpret(bindings) || rightOperand.interpret(bindings);
  }

  @Override
  public Operation copy() {
    return new Or();
  }

  @Override
  public int parse(String[] tokens, int pos, Deque<Expression> stack) {
    var left = stack.pop();
    int i = findNextExpression(tokens, pos + 1, stack);
    var right = stack.pop();
    this.leftOperand = left;
    this.rightOperand = right;
    stack.push(this);
    return i;
  }
}
