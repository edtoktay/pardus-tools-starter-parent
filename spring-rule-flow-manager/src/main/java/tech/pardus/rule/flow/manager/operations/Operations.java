/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public enum Operations {
  /**
   * 
   */
  INSTANCE;

  private final Map<String, Operation> operations = new HashMap<>();

  /**
   * Register any defined operation in system with a symbol
   * 
   * @param op
   * @param symbol
   */
  public void registerOperation(Operation op, String symbol) {
    if (!operations.containsKey(symbol)) {
      operations.put(symbol, op);
    }
  }

  /**
   * Register any defined operation
   * 
   * @param op
   */
  public void registerOperation(Operation op) {
    if (!operations.containsKey(op.getSymbol())) {
      operations.put(op.getSymbol(), op);
    }
  }

  /**
   * Register all defined operations
   */
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

  /**
   * @param symbol
   * @return get the operation defined by symbol
   */
  public Operation getOperation(String symbol) {
    return this.operations.get(symbol);
  }

  /**
   * @return get all defined symbols
   */
  public Set<String> getDefinedSymbols() {
    return this.operations.keySet();
  }
}
