/**
 *
 */
package tech.pardus.rule.flow.manager.operations;

import java.util.Stack;

import tech.pardus.rule.flow.manager.expressions.BaseType;
import tech.pardus.rule.flow.manager.expressions.Expression;
import tech.pardus.rule.flow.manager.expressions.Variable;

/**
 * @author deniz.toktay
 * @since Sep 11, 2020
 */
public abstract class AbstractOperation extends Operation {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AbstractOperation(String symbol) {
		super(symbol);
	}

	@Override
	public int parse(String[] tokens, int pos, Stack<Expression> stack) {
		if (pos - 1 >= 0 && tokens.length >= pos + 1) {
			var var = tokens[pos - 1];
			this.leftOperand = new Variable(var);
			this.rightOperand = BaseType.getBaseType(tokens[pos + 1]);
			stack.push(this);
			return pos + 1;
		}
		throw new IllegalArgumentException("Cannot assign value to variable");
	}

}
