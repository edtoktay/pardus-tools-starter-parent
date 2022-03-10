/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.Deque;
import java.util.Map;
import tech.pardus.rule.flow.manager.expressions.Expression;

/**
 * Predefined And (defined with AND symbol) operation && in given objects preferred to use by
 * boolean values
 * 
 * @author deniztoktay
 * @since Aug 16, 2020
 */
public class And extends Operation {

  /** */
  private static final long serialVersionUID = -9071753385364905452L;

  /**
   * 
   */
  public And() {
    super("AND");
  }

  @Override
  public boolean interpret(Map<String, ?> bindings) {
    return leftOperand.interpret(bindings) && rightOperand.interpret(bindings);
  }

  @Override
  public Operation copy() {
    return new And();
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
