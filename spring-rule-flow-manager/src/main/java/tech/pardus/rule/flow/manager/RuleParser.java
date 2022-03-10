/** */
package tech.pardus.rule.flow.manager;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
import static org.apache.commons.lang3.StringUtils.stripStart;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

  private RuleParser() {

  }

  private static Node<RulePart> handleIfStatement(Node<RulePart> headNode,
      Pair<Integer, String> rulePart, String strippedExpression) {
    var modelBuilder = ExpressionModel.expression();
    modelBuilder.isHeadExpression();
    modelBuilder.expression(RuleStringOperations.extarctExpression(strippedExpression));
    if (Objects.isNull(headNode)) {
      headNode = new Node<>(modelBuilder.addExpression(), rulePart.getKey());
    } else if (rulePart.getKey() == headNode.getLevel()) {
      headNode.addSibling(modelBuilder.addExpression());
      headNode = headNode.getNextSibling();
    } else if (rulePart.getKey() > headNode.getLevel()) {
      headNode.addChild(modelBuilder.addExpression());
      headNode = headNode.getChild();
    } else {
      headNode = findLevelNode(headNode, rulePart.getKey());
      headNode.addSibling(modelBuilder.addExpression());
      headNode = headNode.getNextSibling();
    }
    headNode.setExecutable(true);
    return headNode;
  }

  private static String stripString(String expressionString) {
    var strippedString = expressionString.replaceAll("\n", " ").replaceAll("\r", "");
    strippedString = stripStart(strippedString, " ");
    strippedString = RegExUtils.removeAll(strippedString, "\t");
    return strippedString;
  }

  /**
   * @param in rule string
   * @return head node of the rule
   */
  public static Node<RulePart> ruler(String in) {
    Node<RulePart> headNode = null;
    Node<RulePart> currentNode = null;
    var skel = parseSkeleton(in);
    for (var sk : skel) {
      var modelBuilder = ExpressionModel.expression();
      var strippedString = stripString(sk.getRight());
      switch (getKeyword(strippedString)) {
        case 0:
          if (Objects.isNull(headNode)) {
            headNode = currentNode = handleIfStatement(headNode, sk, strippedString);
          } else {
            currentNode = handleIfStatement(currentNode, sk, strippedString);
          }
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
          if (Objects.isNull(currentNode)) {
            throw new InvalidRuleStructure();
          }
          currentNode = handleDispatcherAction(currentNode, sk, actionBuilder);
          break;
      }
    }
    return headNode;
  }

  private static Node<RulePart> handleDispatcherAction(Node<RulePart> currentNode,
      Pair<Integer, String> sk, ActionBuilder actionBuilder) {
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
    return currentNode;
  }

  private static void handleAction(ActionBuilder actionBuilder, String strippedString) {
    actionBuilder.dispatcher(RuleStringOperations.extractStatementBetweenBrackets(strippedString));
    RuleStringOperations.extractArgs(strippedString).forEach(actionBuilder::withArg);
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
    return IntStream.range(0, keywords.size())
        .filter(i -> startsWithIgnoreCase(in, keywords.get(i))).findFirst()
        .orElseThrow(InvalidRuleStructure::new);
  }

  private static List<Pair<Integer, String>> parseSkeleton(String in) {
    var ruleParts = new ArrayList<Pair<Integer, String>>();
    var stack = new ArrayDeque<Character>();
    var iterator = new StringCharacterIterator(in);
    var temp = "";
    var sb = new StringBuilder();
    while (iterator.current() != CharacterIterator.DONE) {
      switch (iterator.current()) {
        case CURLY_BRACKET_OPEN -> {
          stack.push(CURLY_BRACKET_OPEN);
          if (isNotBlank(sb.toString())) {
            temp = sb.toString();
            sb = new StringBuilder();
          }
        }
        case CURLY_BRACKET_CLOSE -> {
          if (isBlank(temp)) {
            temp = sb.toString();
            sb = new StringBuilder();
          } else {
            ruleParts.add(Pair.of(Integer.valueOf(stack.size() + 1), temp));
            temp = null;
          }
          stack.pop();
        }
        case SEMI_COLON -> {
          sb.append(iterator.current());
          ruleParts.add(Pair.of(Integer.valueOf(stack.size() + 1), sb.toString()));
          sb = new StringBuilder();
        }
        default -> sb.append(iterator.current());
      }
      if (isNotBlank(temp)) {
        ruleParts.add(Pair.of(Integer.valueOf(stack.size()), temp));
        temp = null;
      }
      iterator.next();
    }
    return ruleParts;
  }
}
