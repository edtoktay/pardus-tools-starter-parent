/**
 *
 */
package tech.pardus.multitenant.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableMultiTenancy {

	boolean enable() default false;

	DataSource[] datasources() default {};

	String[] entityPackages() default {};

}
