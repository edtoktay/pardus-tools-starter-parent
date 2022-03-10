/** */
package tech.pardus.rule.flow.manager.actions;

import java.io.Serializable;

/**
 * @author deniz.toktay
 * @since Sep 11, 2020
 */
@FunctionalInterface
public interface ActionDispatcher extends Serializable {

  /**
   * Run method for action dispatched for given String arguments
   * 
   * @param args
   */
  void fire(String... args);
}
