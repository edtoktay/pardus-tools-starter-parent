/** */
package tech.pardus.rule.flow.manager.test;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import tech.pardus.rule.flow.manager.models.RuleModel;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
@SpringBootTest(classes = { App.class })
class ParserTest {

	// @formatter:off
  static String in2 =
      "IF (ACTIVE_USER.AGENCY_CODE GT 300000 AND ACTIVE_USER.AGENCY_CODE LT 350000) {"
          + "	IF (ACTIVE_USER.USER_TYPE EQ 'Agencies Of Hede') {"
          + "		IF (ACTIVE_USER.USER_ROLE EQ 'YONETICI') {"
          + "			IF (ACTIVE_USER.IS_ALT_AGENCY_USER EQ true) {"
          + "				EXEC(ADD_ROLE_GROUP) -> ['ALT_AGENCY_MANAGER_ROLE_GROUP', ACTIVE_USER.AGENCY_CODE ];EXEC(ADD_ROLE)->['ASK_DEVICE_PERMISSION'];"
          + "			} ELIF (ACTIVE_USER.IS_PLAZA_AGENCY EQ true){"
          + "				EXEC(ADD_ROLE_GROUP)->['PLAZA_AGENCY_MANAGER_ROLE_GROUP'];"
          + "			} ELSE {"
          + "				EXEC(ADD_ROLE_GROUP)->['AGENCY_MANAGER_ROLE_GROUP'];"
          + "			}"
          + "			EXEC(REMOVE_ROLE)->['ASK_DEVICE_PERMISSION'];"
          + "		} ELSE {"
          + "			EXEC(ADD_ROLE)->['AGENCY_BASIC_ROLE'];"
          + "			IF (ACTIVE_USER.IS_KNOWN_IP NOT EQ true AND ACTIVE_USER.IS_MOBILE NOT EQ true) {"
          + "				EXEC(REMOVE_ROLE)->['PRODUCE_POLICY'];"
          + "			} ELIF (ACTIVE_USER.IS_KNOWN_IP NOT EQ true AND ACTIVE_USER.IS_MOBILE EQ true) {"
          + "				IF (ACTIVE_USER.KNOWN_DEVICE NOT EQ true){"
          + "					EXEC(REMOVE_ROLE)->['PRODUCE_POLICY'];"
          + "				}"
          + "			} ELSE {"
          + "				EXEC(REMOVE_ROLE)->['ASK_DEVICE_PERMISSION'];"
          + "			}"
          + "			IF (ACTIVE_USER.SYS_USER CONTAIN '5'] {"
          + "				IF (ACTIVE_USER.IS_ALT_AGENCY_USER NOT EQ true AND ACTIVE_USER.IS_PLAZA_AGENCY EQ true){"
          + "					EXEC(ADD_ROLE)->['VIEW_PLAZA_AGENCY_REPORTS_ROLE'];"
          + "				} ELIF(ACTIVE_USER.IS_ALT_AGENCY_USER NOT EQ true) {"
          + "					EXEC(ADD_ROLE)->['VIEW_AGENCY_REPORTS_ROLE'];"
          + "				}"
          + "			}"
          + "		}"
          + "	} ELSE {"
          + "		EXEC(EXCEPTIONER)->['user_no_right_to_use_2'];"
          + "	}"
          + "} ELSE {"
          + "	EXEC(EXCEPTIONER)->['user_no_right_to_use_1'];"
          + "}";

  static Map<String, ?> bindings =
      Map.ofEntries(
          Map.entry("ACTIVE_USER.AGENCY_CODE", 320000),
          Map.entry("ACTIVE_USER.USER_TYPE", "Agencies Of Hede"),
          Map.entry("ACTIVE_USER.USER_ROLE", "YONETICI"),
          Map.entry("ACTIVE_USER.IS_ALT_AGENCY_USER", true));
  // @formatter:on

	@Test
	void tester() {
		var ruleModel = RuleModel.rule().name("HEDE").rule(in2).addRule();
		ruleModel.processRule(bindings);
	}
	// @Test
	// void test() {
	// var operations = Operations.INSTANCE;
	// operations.registerAllOperations();
	// RuleParser.ruler(in2);
	// var structure = RuleParser.parseSkeleton(in2);
	// assertNotNull(structure);
	// }

}
