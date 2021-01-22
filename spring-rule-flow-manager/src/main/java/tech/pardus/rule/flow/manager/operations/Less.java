/** */
package tech.pardus.rule.flow.manager.operations;

import java.util.Map;

import tech.pardus.rule.flow.manager.expressions.BaseType;
import tech.pardus.rule.flow.manager.expressions.Variable;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public class Less extends AbstractOperation {

  /** */
  private static final long serialVersionUID = -8226306857832636954L;

  public Less() {
    super("LT");
  }

  @Override
  public boolean interpret(Map<String, ?> bindings) {
    var v = (Variable) this.leftOperand;
    var obj = bindings.get(v.getName());
    if (obj == null) {
      return false;
    }
    BaseType<?> type = (BaseType<?>) this.rightOperand;
    if (type.getType().equals(obj.getClass())) {
      if (Integer.class.isAssignableFrom(type.getType())) {
        return (Integer) type.value > (Integer) obj;
      } else if (Float.class.isAssignableFrom(type.getType())) {
        return (Float) type.value > (Float) obj;
      }
    }
    return false;
  }

  @Override
  public Operation copy() {
    return new Less();
  }
}
