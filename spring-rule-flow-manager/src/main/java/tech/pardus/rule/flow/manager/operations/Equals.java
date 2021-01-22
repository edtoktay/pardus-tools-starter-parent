/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.Map;

import tech.pardus.rule.flow.manager.expressions.BaseType;
import tech.pardus.rule.flow.manager.expressions.Variable;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public class Equals extends AbstractOperation {

  /** */
  private static final long serialVersionUID = 985436148962840662L;

  public Equals() {
    super("EQ");
  }

  @Override
  public Equals copy() {
    return new Equals();
  }

  @Override
  public boolean interpret(Map<String, ?> bindings) {
    var v = (Variable) this.leftOperand;
    var obj = bindings.get(v.getName());
    if (obj == null) {
      return false;
    }
    BaseType<?> type = (BaseType<?>) this.rightOperand;
    return type.getType().equals(obj.getClass()) && type.getValue().equals(obj);
  }
}
