/** */
package tech.pardus.rule.flow.manager;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import tech.pardus.rule.flow.manager.expressions.Expression;
import tech.pardus.rule.flow.manager.operations.Operations;

/**
 * Parse Rule Strings to Expression and Action Objects
 * 
 * @author deniz.toktay
 * @since Aug 17, 2020
 */
public class RuleStringOperations {

  private static final char CURLY_BRACKET_OPEN = '{';

  private static final char CURLY_BRACKET_CLOSE = '}';

  private static final char BRACKET_OPEN = '(';

  private static final char BRACKET_CLOSE = ')';

  private static final char SQUARE_BRACKET_OPEN = '[';

  private static final char SQUARE_BRACKET_CLOSE = ']';

  private static final String APHS = "'";

  private static final String COMMA = ",";

  private static final Operations operations = Operations.INSTANCE;

  private RuleStringOperations() {}

  /**
   * Remove from any expression or action statement in Rule String
   * 
   * @param string
   * @return Stripped String
   */
  public static String stripeStringFromAphs(String string) {
    string = string.strip();
    var startIdx = 0;
    var endIdx = string.length();
    if (string.startsWith(APHS)) {
      startIdx = 1;
    }
    if (string.endsWith(APHS)) {
      endIdx = endIdx - 1;
    }
    return string.substring(startIdx, endIdx);
  }

  /**
   * List of expression or statment string based on their level
   * 
   * @param in Rule String
   * @return Linked list of the parsed rule pairs left of the pair is the level of statement second
   *         is the String value to generate leveled rule part objects.
   */
  public static List<Pair<Integer, String>> parseSkeleton(String in) {
    var ruleParts = new LinkedList<Pair<Integer, String>>();
    var stack = new Stack<Character>();
    var iterator = new StringCharacterIterator(in);
    var temp = "";
    var sb = new StringBuilder();
    while (iterator.current() != CharacterIterator.DONE) {
      if (iterator.current() == CURLY_BRACKET_OPEN) {
        stack.push(CURLY_BRACKET_OPEN);
        if (StringUtils.isNotBlank(sb.toString())) {
          temp = sb.toString();
          sb = new StringBuilder();
        }
      } else if (iterator.current() == CURLY_BRACKET_CLOSE) {
        if (StringUtils.isBlank(temp)) {
          temp = sb.toString();
          sb = new StringBuilder();
        }
        if (StringUtils.isNotBlank(temp)) {
          ruleParts.add(Pair.of(Integer.valueOf(stack.size() + 1), temp));
          temp = null;
        }
        stack.pop();
      } else {
        sb.append(iterator.current());
      }
      if (StringUtils.isNotBlank(temp)) {
        ruleParts.add(Pair.of(Integer.valueOf(stack.size()), temp));
        temp = null;
      }
      iterator.next();
    }
    return ruleParts;
  }

  /**
   * Generate Expression object from given String
   * 
   * @param in
   * @return Expression Object
   */
  public static Expression extarctExpression(String in) {
    return expressionFromString(extractStatementBetweenBrackets(in));
  }

  /**
   * Extract single or multiple statement String from raw statement String
   * 
   * @param in
   * @return parsed statement string
   */
  public static String extractStatementBetweenBrackets(String in) {
    var iterator = new StringCharacterIterator(in);
    var sb = new StringBuilder();
    var control = false;
    while (iterator.current() != CharacterIterator.DONE) {
      if (iterator.current() == BRACKET_CLOSE) {
        break;
      } else if (control) {
        sb.append(iterator.current());
      } else if (iterator.current() == BRACKET_OPEN) {
        control = true;
      }
      iterator.next();
    }
    return sb.toString();
  }

  /**
   * Extract single or multiple statement values String from Square Brackets from raw statement
   * String
   * 
   * @param in
   * @return Extracted statement values Strings
   */
  public static String extractStatementBetweenSquareBrackets(String in) {
    var iterator = new StringCharacterIterator(in);
    var sb = new StringBuilder();
    var control = false;
    while (iterator.current() != CharacterIterator.DONE) {
      if (iterator.current() == SQUARE_BRACKET_CLOSE) {
        break;
      } else if (control) {
        sb.append(iterator.current());
      } else if (iterator.current() == SQUARE_BRACKET_OPEN) {
        control = true;
      }
      iterator.next();
    }
    return sb.toString();
  }

  /**
   * Extract arguments from square bracket extracted String
   * 
   * @param in
   * @return List of Arguments for an action
   */
  public static List<String> extractArgs(String in) {
    if (StringUtils.isBlank(in)) {
      return new ArrayList<>();
    }
    var extractedArgs = extractStatementBetweenSquareBrackets(in);
    var splitted = StringUtils.split(extractedArgs, COMMA);
    return Stream.of(splitted).map(RuleStringOperations::stripeStringFromAphs).toList();
  }

  /**
   * Extract Expressions from Stripped String
   * 
   * @param expr
   * @return Expression Object for given String
   */
  public static Expression expressionFromString(String expr) {
    var tokenList = new ArrayList<String>();
    var stack = new ArrayDeque<Expression>();
    var regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    var matcher = regex.matcher(expr);
    while (matcher.find()) {
      tokenList.add(matcher.group());
    }
    var tokens = tokenList.stream().toArray(String[]::new);
    for (int i = 0; i < tokens.length - 1; i++) {
      var op = operations.getOperation(tokens[i]);
      if (op != null) {
        op = op.copy();
        i = op.parse(tokens, i, stack);
      }
    }
    return stack.pop();
  }

}
