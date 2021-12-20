/**
 *
 */
package tech.pardus.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author edtoktay
 * @since Dec 20, 2021
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredEndPoint {

  String accessKey() default "";

  String[] roles() default {};

  boolean publicUrl() default false;
}
