/** */
package tech.pardus.rule.flow.manager.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author deniz.toktay
 * @since Aug 17, 2020
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FactBean {

  @SuppressWarnings("javadoc")
  String name();
}
