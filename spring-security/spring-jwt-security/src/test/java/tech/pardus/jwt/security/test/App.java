/** */
package tech.pardus.jwt.security.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author deniz.toktay
 * @since Sep 27, 2020
 */
@SpringBootApplication
@ComponentScan("tech.pardus.jwt.security")
public class App {

  /** @param args */
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
