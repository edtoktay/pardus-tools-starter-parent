/**
 *
 */
package tech.pardus.rule.flow.manager;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import tech.pardus.rule.flow.manager.actions.ActionDispatcher;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
@Component
public class FlowManagerSpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	public static <T extends ActionDispatcher> T getActionBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	public static Object getBean(Class<?> beanClass) {
		return context.getBean(beanClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		FlowManagerSpringContext.context = context;
	}

}
