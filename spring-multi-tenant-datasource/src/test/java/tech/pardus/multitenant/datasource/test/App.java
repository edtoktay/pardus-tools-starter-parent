/**
 *
 */
package tech.pardus.multitenant.datasource.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tech.pardus.multitenant.datasource.EnableMultiTenancy;

/**
 * @author edtoktay
 *
 */
@EnableMultiTenancy
@SpringBootApplication
@ComponentScan("tech.pardus.multitenant.datasource")
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
