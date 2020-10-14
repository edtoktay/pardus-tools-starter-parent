/**
 *
 */
package tech.pardus.rule.flow.manager.annotattions;

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
@Target(ElementType.METHOD)
public @interface Fact {

	String name();

	String description() default "";

}
