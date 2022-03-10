/**
 *
 */
package tech.pardus.spring.utilities;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author deniztoktay
 * @since Jan 8, 2022
 */
@Configuration("springBeanAnnotationScanner")
@AutoConfigurationPackage
public class SpringBeanAnnotationScanner implements ApplicationContextAware {

  private static final String BEAN_NAME = "springBeanAnnotationScanner";
  private ApplicationContext applicationContext;

  /**
   * Set of the Spring Beans which has given annotation.
   *
   * @param annotation
   * @return Set of classes, which presents given annotation
   */
  public Set<Class<?>> getAnnotatedBeans(Class<? extends Annotation> annotation) {
    var classes = new HashSet<Class<?>>();
    for (String beanName : applicationContext.getBeanDefinitionNames()) {
      if (StringUtils.equalsIgnoreCase(beanName, BEAN_NAME)) {
        continue;
      }
      var obj = applicationContext.getBean(beanName);
      Class<?> clazz = obj.getClass();
      if (AopUtils.isAopProxy(obj)) {
        clazz = AopUtils.getTargetClass(obj);
      }
      if (clazz.isAnnotationPresent(annotation)) {
        classes.add(clazz);
      }
    }
    return classes;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
