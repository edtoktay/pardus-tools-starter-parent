/** */
package tech.pardus.rule.flow.manager.actions;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tech.pardus.rule.flow.manager.annotattions.DispatcherBean;

/**
 * @author deniz.toktay
 * @since Sep 11, 2020
 */
@DispatcherBean(name = "Default")
@Component
public class NullActionDispatcher implements ActionDispatcher {

  /** */
  private static final long serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(NullActionDispatcher.class);

  @Override
  public void fire(String... args) {
    logger.debug("Default Null Action Triggred");
    Stream.of(args).forEach(System.out::println);
  }
}
