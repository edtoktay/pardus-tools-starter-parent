/**
 *
 */
package tech.pardus.rule.flow.manager.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Sep 10, 2020
 */
@Getter
@Setter
@NoArgsConstructor
public class ActionModel implements RulePart {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String dispatcherName;

	private List<String> dispatcherArgs = new ArrayList<>();

	public static ActionBuilder action() {
		return new ActionModel.ActionBuilder();
	}

	/**
	 * @author deniz.toktay
	 * @since Sep 10, 2020
	 */
	@NoArgsConstructor
	public static class ActionBuilder {

		private final ActionModel managedInstance = new ActionModel();

		public ActionBuilder dispatcher(String dispatcherName) {
			managedInstance.dispatcherName = dispatcherName;
			return this;
		}

		public ActionBuilder withArg(String object) {
			if (CollectionUtils.isEmpty(managedInstance.dispatcherArgs)) {
				managedInstance.dispatcherArgs = new ArrayList<>();
			}
			managedInstance.dispatcherArgs.add(object);
			return this;
		}

		public ActionModel addAction() {
			return managedInstance;
		}

	}

	public String[] getArgs() {
		return CollectionUtils.isEmpty(dispatcherArgs) ? null
		        : getDispatcherArgs().toArray(new String[getDispatcherArgs().size()]);
	}

	@Override
	public RulePartType getTypeOfRule() {
		return RulePartType.ACTION;
	}

}
