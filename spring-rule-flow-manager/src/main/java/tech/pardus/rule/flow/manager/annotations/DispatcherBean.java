/** */
package tech.pardus.rule.flow.manager.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Dispatcher Bean Registration bean names should be registered and give name dispatcher fire method
 * will be triggered in rule manager
 * 
 * @author deniz.toktay
 * @since Aug 17, 2020
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DispatcherBean {

  /*
   * Name of the action dispatcher used in rule
   */
  String name();
}
