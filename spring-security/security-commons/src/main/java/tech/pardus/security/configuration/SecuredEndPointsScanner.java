/**
 *
 */
package tech.pardus.security.configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.pardus.security.annotation.SecuredEndPoint;

/**
 * @author edtoktay
 * @since Dec 20, 2021
 */
@Configuration("securedEndPointsScanner")
public class SecuredEndPointsScanner implements ApplicationContextAware {

  private static final String BEAN_NAME = "securedEndPointsScanner";

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    for (String beanName : applicationContext.getBeanDefinitionNames()) {
      if (StringUtils.equalsIgnoreCase(beanName, BEAN_NAME)) {
        continue;
      }
      var obj = applicationContext.getBean(beanName);
      Class<?> clazz = obj.getClass();
      if (AopUtils.isAopProxy(obj)) {
        clazz = AopUtils.getTargetClass(obj);
      }
      if (clazz.isAnnotationPresent(RestController.class)) {
        handleClass(clazz);
      }
    }
  }

  private void handleClass(Class<?> targetClass) {
    var registry = ModuleUrlSecurityRegistry.registry();
    var classMapping = targetClass.getAnnotation(RequestMapping.class) == null ? ""
        : targetClass.getAnnotation(RequestMapping.class).value() == null ? ""
            : targetClass.getAnnotation(RequestMapping.class).value().length > 0
                ? targetClass.getAnnotation(RequestMapping.class).value()[0]
                : targetClass.getAnnotation(RequestMapping.class).path().length > 0
                    ? targetClass.getAnnotation(RequestMapping.class).path()[0]
                    : "";
    for (var method : targetClass.getMethods()) {
      var methodMapping = handleMethodPath(method, classMapping);
      if (methodMapping != null) {
        var presentedSecurityAnnotation = method.getAnnotation(SecuredEndPoint.class);
        if (presentedSecurityAnnotation != null) {
          registry.registerNewUrl(methodMapping.getRight(), methodMapping.getLeft(),
              presentedSecurityAnnotation.accessKey(),
              ArrayUtils.isNotEmpty(presentedSecurityAnnotation.roles())
                  ? Arrays.asList(presentedSecurityAnnotation.roles())
                  : null,
              !presentedSecurityAnnotation.publicUrl());
        } else {
          registry.registerNewUrl(methodMapping.getRight(), methodMapping.getLeft(), null, null,
              false);
        }
      }
    }
  }

  private Pair<String, RequestMethod> handleMethodPath(Method method, String classMapping) {
    // @formatter:off
    try {
      for (var annot : method.getAnnotations()) {
        if (annot.annotationType().isAssignableFrom(RequestMapping.class)) {
          var methodUrl =
              ((RequestMapping) annot).value().length > 0
                  ? ((RequestMapping) annot).value()[0]
                  : ((RequestMapping) annot).path().length > 0
                      ? ((RequestMapping) annot).path()[0]
                      : "";
          var methodType = ((RequestMapping) annot).method()[0];
          return Pair.of(classMapping + methodUrl, methodType);
        } else if (annot.annotationType().isAssignableFrom(PostMapping.class)) {
          var methodUrl =
              ((PostMapping) annot).value().length > 0
                  ? ((PostMapping) annot).value()[0]
                  : ((PostMapping) annot).path().length > 0 ? ((PostMapping) annot).path()[0] : "";
          var methodType = RequestMethod.POST;
          return Pair.of(classMapping + methodUrl, methodType);
        } else if (annot.annotationType().isAssignableFrom(PutMapping.class)) {
          var methodUrl =
              ((PutMapping) annot).value().length > 0
                  ? ((PutMapping) annot).value()[0]
                  : ((PutMapping) annot).path().length > 0 ? ((PutMapping) annot).path()[0] : "";
          var methodType = RequestMethod.POST;
          return Pair.of(classMapping + methodUrl, methodType);
        } else if (annot.annotationType().isAssignableFrom(DeleteMapping.class)) {
          var methodUrl =
              ((DeleteMapping) annot).value().length > 0
                  ? ((DeleteMapping) annot).value()[0]
                  : ((DeleteMapping) annot).path().length > 0
                      ? ((DeleteMapping) annot).path()[0]
                      : "";
          var methodType = RequestMethod.DELETE;
          return Pair.of(classMapping + methodUrl, methodType);
        } else if (annot.annotationType().isAssignableFrom(GetMapping.class)) {
          var methodUrl =
              ((GetMapping) annot).value().length > 0
                  ? ((GetMapping) annot).value()[0]
                  : ((GetMapping) annot).path().length > 0 ? ((GetMapping) annot).path()[0] : "";
          var methodType = RequestMethod.GET;
          return Pair.of(classMapping + methodUrl, methodType);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    // @formatter:on
    return null;
  }

}
