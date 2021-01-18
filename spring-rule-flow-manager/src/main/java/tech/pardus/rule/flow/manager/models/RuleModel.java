/**
 *
 */
package tech.pardus.rule.flow.manager.models;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tech.pardus.rule.flow.manager.RuleParser;
import tech.pardus.rule.flow.manager.FlowManagerSpringContext;
import tech.pardus.rule.flow.manager.actions.ActionDispatcherManager;
import tech.pardus.rule.flow.manager.datastruture.Node;

/**
 * @author deniz.toktay
 * @since Sep 27, 2020
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class RuleModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String ruleName;

	private Node<RulePart> rule;

	public static RuleBuilder rule() {
		return new RuleModel.RuleBuilder();
	}

	/**
	 * @author deniz.toktay
	 * @since Sep 27, 2020
	 */
	@NoArgsConstructor
	public static class RuleBuilder {

		private final RuleModel managedInstance = new RuleModel();

		public RuleBuilder rule(String in) {
			var rule = RuleParser.ruler(in);
			managedInstance.rule = rule;
			return this;
		}

		public RuleBuilder name(String in) {
			managedInstance.ruleName = in;
			return this;
		}

		public RuleModel addRule() {
			return managedInstance;
		}

	}

	public void processRule(Map<String, ?> bindings) {
		var currentNode = rule;
		currentNode = processCurrentNode(currentNode, bindings);
		processParentSiblings(currentNode, bindings);
	}

	private Node<RulePart> processCurrentNode(Node<RulePart> currentNode, Map<String, ?> bindings) {
		var moveType = moveCursor(currentNode, bindings);
		Node<RulePart> tempNode = null;
		while (moveType != MoveType.STOP) {
			switch (moveType) {
			case CHILD:
				tempNode = currentNode.getChild();
				break;
			default:
				tempNode = currentNode.getNextSibling();
				break;
			}
			if (Objects.isNull(tempNode)) {
				moveType = MoveType.STOP;
			} else {
				currentNode = tempNode;
				moveType = moveCursor(currentNode, bindings);
			}
		}
		return currentNode;
	}

	private void processParentSiblings(Node<RulePart> currentNode, Map<String, ?> bindings) {
		var temp = findParent(currentNode);
		while (Objects.nonNull(temp)) {
			processExecutableSibling(temp, bindings);
			temp = findParent(temp);
		}
	}

	private Node<RulePart> findParent(Node<RulePart> parent) {
		while (Objects.isNull(parent.getParent())) {
			if (Objects.isNull(parent.getPrevSibling())) {
				parent = null;
				break;
			}
			parent = parent.getPrevSibling();
		}
		return Objects.nonNull(parent) ? parent.getParent() : null;
	}

	private void processExecutableSibling(Node<RulePart> temp, Map<String, ?> bindings) {
		while (Objects.nonNull(temp)) {
			temp = temp.getNextSibling();
			if (Objects.nonNull(temp) && temp.isExecutable()) {
				processCurrentNode(temp, bindings);
			}
		}
	}

	private MoveType moveCursor(Node<RulePart> currentNode, Map<String, ?> bindings) {
		if (currentNode.getData().getTypeOfRule() == RulePartType.STATEMENT) {
			return processNode(currentNode, bindings) ? MoveType.CHILD : MoveType.SIBLING;
		} else {
			processNode(currentNode, bindings);
		}
		return MoveType.SIBLING;
	}

	private boolean processNode(Node<RulePart> currentNode, Map<String, ?> bindings) {
		var control = false;
		switch (currentNode.getData().getTypeOfRule()) {
		case STATEMENT:
			control = processExpression(currentNode, bindings);
			break;
		default:
			try {
				processDispatcher(currentNode);
				control = true;
			} catch (Exception e) {
				log.error("Dispatcher Running Error ", e);
			}
			break;
		}
		return control;
	}

	private boolean processExpression(Node<RulePart> currentNode, Map<String, ?> bindings) {
		var expressionModel = (ExpressionModel) currentNode.getData();
		return expressionModel.isElseExpression() ? true : expressionModel.getExpression().interpret(bindings);
	}

	private void processDispatcher(Node<RulePart> currentNode) throws Exception {
		var actionModel = (ActionModel) currentNode.getData();
		var managerBean = (ActionDispatcherManager) FlowManagerSpringContext.getBean(ActionDispatcherManager.class);
		managerBean.runDispatcher(actionModel.getDispatcherName(), actionModel.getArgs());
	}

	enum MoveType {
		CHILD, SIBLING, STOP;
	}

}
