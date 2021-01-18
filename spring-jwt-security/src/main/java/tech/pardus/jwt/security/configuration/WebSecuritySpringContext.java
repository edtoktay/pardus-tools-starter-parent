/**
 *
 */
package tech.pardus.jwt.security.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
@Component
public class WebSecuritySpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	public static <T extends Object> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) {
		WebSecuritySpringContext.context = context;
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

}
