/**
 *
 */
package tech.pardus.rule.flow.manager.expressions;

import java.util.Map;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public class Variable implements Expression {

	/**
	 *
	 */
	private static final long serialVersionUID = -8875892131840710621L;

	private String name;

	public Variable(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean interpret(Map<String, ?> bindings) {
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
