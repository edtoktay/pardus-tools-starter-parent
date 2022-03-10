/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.Stack;
import tech.pardus.rule.flow.manager.expressions.Expression;

/**
 * Abstract class for each Rule Operation
 * 
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public abstract class Operation implements Expression {

  /** */
  private static final long serialVersionUID = 1071344443835770460L;

  protected final String symbol;

  protected Expression leftOperand = null;

  protected Expression rightOperand = null;

  /**
   * Operation Constructor
   * 
   * @param symbol
   */
  public Operation(String symbol) {
    this.symbol = symbol;
  }

  /**
   * @return Copy Of Operation
   */
  public abstract Operation copy();

  /**
   * @param tokens
   * @param pos
   * @param stack
   * @return
   */
  public abstract int parse(final String[] tokens, final int pos, final Stack<Expression> stack);

  protected Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack) {
    var operations = Operations.INSTANCE;
    for (int i = pos; i < tokens.length; i++) {
      var op = operations.getOperation(tokens[i]);
      if (op != null) {
        op = op.copy();
        i = op.parse(tokens, i, stack);
        return i;
      }
    }
    return null;
  }

  /**
   * @return
   */
  public String getSymbol() {
    return symbol;
  }
}
