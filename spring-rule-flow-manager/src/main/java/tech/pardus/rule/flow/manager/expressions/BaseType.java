/** */
package tech.pardus.rule.flow.manager.expressions;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tech.pardus.rule.flow.manager.RuleStringOperations;

/**
 * @author deniz.toktay
 * @since Aug 16, 2020
 */
public class BaseType<T> implements Expression {

  /** */
  private static final long serialVersionUID = -4668383226200294418L;

  public T value;

  public Class<T> type;

  public BaseType(T value, Class<T> type) {
    super();
    this.value = value;
    this.type = type;
  }

  @Override
  public boolean interpret(Map<String, ?> bindings) {
    return true;
  }

  public static BaseType<?> getBaseType(String type) {
    if (StringUtils.isBlank(type)) {
      throw new IllegalArgumentException("The provided string must not be null");
    }
    if ("true".equals(type) || "false".equals(type)) {
      return new BaseType<>("true".equals(type), Boolean.class);
    } else if (type.startsWith("'")) {
      return new BaseType<>(RuleStringOperations.stripeStringFromAphs(type), String.class);
    } else if (type.contains(".")) {
      return new BaseType<>(Float.parseFloat(type), Float.class);
    } else {
      return new BaseType<>(Integer.parseInt(type), Integer.class);
    }
  }

  public T getValue() {
    return value;
  }

  public Class<T> getType() {
    return type;
  }
}
