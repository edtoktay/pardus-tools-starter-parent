/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.Map;
import java.util.Stack;

import tech.pardus.rule.flow.manager.expressions.Expression;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public class Not extends Operation {

  /** */
  private static final long serialVersionUID = -7859824073707215076L;

  public Not() {
    super("NOT");
  }

  @Override
  public Not copy() {
    return new Not();
  }

  @Override
  public int parse(String[] tokens, int pos, Stack<Expression> stack) {
    int i = findNextExpression(tokens, pos + 1, stack);
    var right = stack.pop();
    this.rightOperand = right;
    stack.push(this);
    return i;
  }

  @Override
  public boolean interpret(final Map<String, ?> bindings) {
    return !this.rightOperand.interpret(bindings);
  }
}
