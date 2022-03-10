/** */
package tech.pardus.rule.flow.manager.operations;

import static tech.pardus.rule.flow.manager.RuleStringOperations.stripeStringFromAphs;
import java.util.Map;
import tech.pardus.rule.flow.manager.expressions.BaseType;
import tech.pardus.rule.flow.manager.expressions.Variable;

/**
 * Predefined Contains (defined with CONTAIN symbol) operation checks whether given String in
 * bindings contains in the rule given String
 * 
 * @author deniz.toktay
 * @since Aug 17, 2020
 */
public class Contains extends AbstractOperation {

  /** */
  private static final long serialVersionUID = -6005786626149113609L;

  /**
   * 
   */
  public Contains() {
    super("CONTAIN");
  }

  @Override
  public boolean interpret(Map<String, ?> bindings) {
    var v = (Variable) this.leftOperand;
    var obj = bindings.get(v.getName());
    if (obj == null) {
      return false;
    }
    BaseType<?> type = (BaseType<?>) this.rightOperand;
    if (type.getType().equals(obj.getClass()) && String.class.isAssignableFrom(type.getType())) {
      return stripeStringFromAphs((String) obj)
          .contains(stripeStringFromAphs((String) type.getValue()));
    }
    return false;
  }

  @Override
  public Operation copy() {
    return new Contains();
  }
}
