/** */
package tech.pardus.rule.flow.manager.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author deniz.toktay
 * @since Sep 10, 2020
 */
@Getter
@Setter
@NoArgsConstructor
public class ActionModel implements RulePart {

  /** */
  private static final long serialVersionUID = 1L;

  private String dispatcherName;

  private List<String> dispatcherArgs = new ArrayList<>();

  /**
   * @return ActionBuilder
   */
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

    /**
     * @param dispatcherName
     * @return ActionBuilder
     */
    public ActionBuilder dispatcher(String dispatcherName) {
      managedInstance.dispatcherName = dispatcherName;
      return this;
    }

    /**
     * @param object
     * @return ActionBuilder
     */
    public ActionBuilder withArg(String object) {
      if (CollectionUtils.isEmpty(managedInstance.dispatcherArgs)) {
        managedInstance.dispatcherArgs = new ArrayList<>();
      }
      managedInstance.dispatcherArgs.add(object);
      return this;
    }

    /**
     * @return ActionModel
     */
    public ActionModel addAction() {
      return managedInstance;
    }
  }

  /**
   * @return array of Action's arguments
   */
  public String[] getArgs() {
    return CollectionUtils.isEmpty(dispatcherArgs) ? null
        : getDispatcherArgs().toArray(new String[getDispatcherArgs().size()]);
  }

  @Override
  public RulePartType getTypeOfRule() {
    return RulePartType.ACTION;
  }
}
