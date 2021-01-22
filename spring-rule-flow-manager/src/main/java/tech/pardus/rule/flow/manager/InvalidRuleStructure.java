/** */
package tech.pardus.rule.flow.manager;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
public class InvalidRuleStructure extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;

  public InvalidRuleStructure() {
    super();
  }

  public InvalidRuleStructure(String message) {
    super(message);
  }
}
