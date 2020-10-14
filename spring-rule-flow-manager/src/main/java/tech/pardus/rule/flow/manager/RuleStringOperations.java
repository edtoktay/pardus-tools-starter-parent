/**
 *
 */
package tech.pardus.rule.flow.manager;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import tech.pardus.rule.flow.manager.expressions.Expression;
import tech.pardus.rule.flow.manager.operations.Operations;

/**
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

	private RuleStringOperations() {
	}

	public static String stripeStringFromAphs(String string) {
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

	public static Expression extarctExpression(String in) {
		return expressionFromString(extractStatementBetweenBrackets(in));
	}

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

	public static List<String> extractArgs(String in) {
		if (StringUtils.isBlank(in)) {
			return new ArrayList<>();
		}
		var extractedArgs = extractStatementBetweenSquareBrackets(in);
		var splitted = StringUtils.split(extractedArgs, COMMA);
		return Stream.of(splitted).map(t -> stripeStringFromAphs(t)).collect(Collectors.toList());
	}

	public static Expression expressionFromString(String expr) {
		var stack = new Stack<Expression>();
		var tokens = expr.split("\\s");
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
