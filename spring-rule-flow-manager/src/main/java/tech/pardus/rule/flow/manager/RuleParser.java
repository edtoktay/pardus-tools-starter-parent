/**
 *
 */
package tech.pardus.rule.flow.manager;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
import static org.apache.commons.lang3.StringUtils.stripStart;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.tuple.Pair;

import tech.pardus.rule.flow.manager.datastruture.Node;
import tech.pardus.rule.flow.manager.models.ActionModel;
import tech.pardus.rule.flow.manager.models.ActionModel.ActionBuilder;
import tech.pardus.rule.flow.manager.models.ExpressionModel;
import tech.pardus.rule.flow.manager.models.RulePart;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
public class RuleParser {

	private static final char CURLY_BRACKET_OPEN = '{';

	private static final char CURLY_BRACKET_CLOSE = '}';

	private static final char SEMI_COLON = ';';

	private static final List<String> keywords = Arrays.asList("IF", "ELIF", "ELSE", "EXEC");

	public static Node<RulePart> ruler(String in) {
		Node<RulePart> headNode = null;
		Node<RulePart> currentNode = null;
		var skel = parseSkeleton(in);
		for (var sk : skel) {
			var modelBuilder = ExpressionModel.expression();
			System.out.println("RAW:  " + sk.getValue());
			var strippedString = stripStart(sk.getRight(), " ");
			strippedString = RegExUtils.removeAll(strippedString, "\t");
			switch (getKeyword(strippedString)) {
			case 0:
				System.out.println("Level: " + sk.getLeft() + " IF STATEMENT: ");
				System.out.println(strippedString);
				modelBuilder.isHeadExpression();
				modelBuilder.expression(RuleStringOperations.extarctExpression(strippedString));
				if (Objects.isNull(headNode)) {
					headNode = new Node<RulePart>(modelBuilder.addExpression(), sk.getKey());
					currentNode = headNode;
				} else if (sk.getKey() == currentNode.getLevel()) {
					currentNode.addSibling(modelBuilder.addExpression());
					currentNode = currentNode.getNextSibling();
				} else if (sk.getKey() > currentNode.getLevel()) {
					currentNode.addChild(modelBuilder.addExpression());
					currentNode = currentNode.getChild();
				} else {
					currentNode = findLevelNode(currentNode, sk.getKey());
					currentNode.addSibling(modelBuilder.addExpression());
					currentNode = currentNode.getNextSibling();
				}
				currentNode.setExecutable(true);
				break;
			case 1:
				modelBuilder.expression(RuleStringOperations.extarctExpression(strippedString));
				currentNode = findLevelNode(currentNode, sk.getKey());
				currentNode.addSibling(modelBuilder.addExpression());
				currentNode = currentNode.getNextSibling();
				break;
			case 2:
				modelBuilder.elseExpression();
				currentNode = findLevelNode(currentNode, sk.getKey());
				currentNode.addSibling(modelBuilder.addExpression());
				currentNode = currentNode.getNextSibling();
				break;
			default:
				var actionBuilder = ActionModel.action();
				handleAction(actionBuilder, strippedString);
				if (sk.getKey() == currentNode.getLevel()) {
					currentNode.addSibling(actionBuilder.addAction());
					currentNode = currentNode.getNextSibling();
				} else if (sk.getKey() > currentNode.getLevel()) {
					currentNode.addChild(actionBuilder.addAction());
					currentNode = currentNode.getChild();
				} else {
					currentNode = findLevelNode(currentNode, sk.getKey());
					currentNode.addSibling(actionBuilder.addAction());
					currentNode = currentNode.getNextSibling();
				}
				currentNode.setExecutable(true);
				break;
			}
		}
		return headNode;
	}

	private static void handleAction(ActionBuilder actionBuilder, String strippedString) {
		actionBuilder.dispatcher(RuleStringOperations.extractStatementBetweenBrackets(strippedString));
		RuleStringOperations.extractArgs(strippedString).forEach(t -> actionBuilder.withArg(t));
	}

	private static Node<RulePart> findLevelNode(Node<RulePart> currentNode, Integer level) {
		while (currentNode != null) {
			currentNode = Objects.nonNull(currentNode.getParent()) ? currentNode.getParent()
			        : currentNode.getPrevSibling();
			if (currentNode.getLevel() == level) {
				return currentNode;
			}
		}
		throw new InvalidRuleStructure();
	}

	private static int getKeyword(String in) {
		return IntStream.range(0, keywords.size()).filter(i -> startsWithIgnoreCase(in, keywords.get(i))).findFirst()
		        .orElseThrow(() -> new InvalidRuleStructure());
	}

	public static List<Pair<Integer, String>> parseSkeleton(String in) {
		var ruleParts = new ArrayList<Pair<Integer, String>>();
		var stack = new Stack<Character>();
		var iterator = new StringCharacterIterator(in);
		var temp = "";
		var sb = new StringBuilder();
		while (iterator.current() != CharacterIterator.DONE) {
			if (iterator.current() == CURLY_BRACKET_OPEN) {
				stack.push(CURLY_BRACKET_OPEN);
				if (isNotBlank(sb.toString())) {
					temp = sb.toString();
					sb = new StringBuilder();
				}
			} else if (iterator.current() == CURLY_BRACKET_CLOSE) {
				if (isBlank(temp)) {
					temp = sb.toString();
					sb = new StringBuilder();
				}
				if (isNotBlank(temp)) {
					// System.out.println("Level: " + (stack.size() + 1) + " " + temp);
					ruleParts.add(Pair.of(Integer.valueOf(stack.size() + 1), temp));
					temp = null;
				}
				stack.pop();
			} else if (iterator.current() == SEMI_COLON) {
				// System.out.println("Level: " + (stack.size() + 1) + " " + sb.toString());
				sb.append(iterator.current());
				ruleParts.add(Pair.of(Integer.valueOf(stack.size() + 1), sb.toString()));
				sb = new StringBuilder();
			} else {
				sb.append(iterator.current());
			}
			if (isNotBlank(temp)) {
				// System.out.println("Level: " + stack.size() + " " + temp);
				ruleParts.add(Pair.of(Integer.valueOf(stack.size()), temp));
				temp = null;
			}
			iterator.next();
		}
		return ruleParts;
	}

}
