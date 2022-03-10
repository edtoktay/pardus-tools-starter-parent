/**
 *
 */
package tech.pardus.spring.utilities;

import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import tech.pardus.utilities.ReflectionUtils;

/**
 * Inject Spring Bean to Non-Spring Beans
 *
 * @author deniz.toktay
 * @since Jan 8, 2021
 */
@Component
public class SpringBeanInjectionContext implements ApplicationContextAware {

  private static ApplicationContext context;

  /**
   * Return the Spring bean for given type <T>
   *
   * @param <T>
   * @param beanClass
   * @return Spring bean of the given class
   * @throws BeansException
   */
  public static <T extends Object> T getBean(Class<T> beanClass) throws BeansException {
    return context.getBean(beanClass);
  }

  /**
   * Return the Spring bean for given name
   *
   * @param beanName
   * @return Spring bean of the given bean name
   * @throws BeansException
   */
  public static Object getBean(String beanName) throws BeansException {
    return context.getBean(beanName);
  }

  /**
   * Run the given bean's given method and return the return of the method's result
   *
   * @param <T>
   * @param beanClass
   * @param methodName
   * @param args
   * @return given bean's given method's return object
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws BeansException
   */
  public static <T> Object runSpringBeanMethod(Class<T> beanClass, String methodName,
      Object... args) throws BeansException, IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    var bean = getBean(beanClass);
    return ReflectionUtils.runMethod(bean, methodName, args);
  }

  /**
   * Run the given bean's given method and return the return of the method's result
   *
   * @param beanName
   * @param methodName
   * @param args
   * @return given bean's given method's return object
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws BeansException
   */
  public static Object runSpringBeanMethod(String beanName, String methodName, Object... args)
      throws BeansException, IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    var bean = getBean(beanName);
    return ReflectionUtils.runMethod(bean, methodName, args);
  }

  @Override
  public void setApplicationContext(ApplicationContext context) {
    SpringBeanInjectionContext.context = context;
  }
}
