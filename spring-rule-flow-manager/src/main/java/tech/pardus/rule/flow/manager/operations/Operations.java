/**
 *
 */
package tech.pardus.rule.flow.manager.operations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public enum Operations {

	INSTANCE;

	private final Map<String, Operation> operations = new HashMap<>();

	public void registerOperation(Operation op, String symbol) {
		if (!operations.containsKey(symbol)) {
			operations.put(symbol, op);
		}
	}

	public void registerOperation(Operation op) {
		if (!operations.containsKey(op.getSymbol())) {
			operations.put(op.getSymbol(), op);
		}
	}

	public void registerAllOperations() {
		registerOperation(new And());
		registerOperation(new Contains());
		registerOperation(new Equals());
		registerOperation(new Greater());
		registerOperation(new GreaterOrEqual());
		registerOperation(new Less());
		registerOperation(new LessOrEqual());
		registerOperation(new Not());
		registerOperation(new Or());
		registerOperation(new StartsWith());
	}

	public Operation getOperation(String symbol) {
		return this.operations.get(symbol);
	}

	public Set<String> getDefinedSymbols() {
		return this.operations.keySet();
	}

}
