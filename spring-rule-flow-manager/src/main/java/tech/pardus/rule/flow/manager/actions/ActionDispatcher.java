/**
 *
 */
package tech.pardus.rule.flow.manager.actions;

import java.io.Serializable;

/**
 * @author deniz.toktay
 * @since Sep 11, 2020
 */
@FunctionalInterface
public interface ActionDispatcher extends Serializable {

	void fire(String... args);

}
