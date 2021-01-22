/** */
package tech.pardus.rule.flow.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import tech.pardus.rule.flow.manager.actions.ActionDispatcherManager;
import tech.pardus.rule.flow.manager.operations.Operations;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan(
    basePackages = {"tech.pardus.rule.flow.manager", "tech.pardus.rule.flow.manager.actions"})
public class SpringFlowManagerConfiguration {

  @Autowired private ActionDispatcherManager dispatcherManager;

  @Bean
  public String managerInit() {
    Operations.INSTANCE.registerAllOperations();
    dispatcherManager.init();
    return "OK";
  }
}
