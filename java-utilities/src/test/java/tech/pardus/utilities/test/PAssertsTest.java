/**
 *
 */
package tech.pardus.utilities.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.Test;

import tech.pardus.utilities.AssertException;
import tech.pardus.utilities.PAsserts;

/**
 * @author deniz.toktay
 * @since Aug 19, 2020
 */
class PAssertsTest {

	@Test
	void test_hasText_1() {
		var val = "";
		assertThrows(AssertException.class, () -> {
			PAsserts.hasText(val);
		});
	}

	@Test
	void test_hasText_1_success() {
		var val = "hede";
		PAsserts.hasText(val);
		assertTrue(true);
	}

	@Test
	void test_hasText_2() {
		var val = "";
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.hasText(val, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_hasText_2_success() {
		var val = "hede";
		PAsserts.hasText(val, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_hasText_3() {
		var val = "";
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.hasText(val, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_hasText_3_success() {
		var val = "hede";
		PAsserts.hasText(val, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_noText1() {
		var val = "a";
		assertThrows(AssertException.class, () -> {
			PAsserts.noText(val);
		});
	}

	@Test
	void test_noText_1_success() {
		var val = "";
		PAsserts.noText(val);
		assertTrue(true);
	}

	@Test
	void test_noText_2() {
		var val = "q";
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.noText(val, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_noText_2_success() {
		var val = "";
		PAsserts.noText(val, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_noText_3() {
		var val = "a";
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.noText(val, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_noText_3_success() {
		var val = "";
		PAsserts.noText(val, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_notEmpty_1() {
		Object[] val = null;
		assertThrows(AssertException.class, () -> {
			PAsserts.notEmpty(val);
		});
	}

	@Test
	void test_notEmpty_1_success() {
		var val = new Object[] { Long.valueOf(0) };
		PAsserts.notEmpty(val);
		assertTrue(true);
	}

	@Test
	void test_notEmpty_2() {
		Object[] val = null;
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.notEmpty(val, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_notEmpty_2_success() {
		var val = new Object[] { Long.valueOf(0) };
		PAsserts.notEmpty(val, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_notEmpty_3() {
		Object[] val = null;
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.notEmpty(val, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_notEmpty_3_success() {
		var val = new Object[] { Long.valueOf(0) };
		PAsserts.notEmpty(val, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_notEmpty_4() {
		var val = new ArrayList<>();
		assertThrows(AssertException.class, () -> {
			PAsserts.notEmpty(val);
		});
	}

	@Test
	void test_notEmpty_4_success() {
		var val = Arrays.asList(Long.valueOf(0));
		PAsserts.notEmpty(val);
		assertTrue(true);
	}

	@Test
	void test_notEmpty_5() {
		var val = new HashSet<>();
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.notEmpty(val, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_notEmpty_5_success() {
		var val = new HashSet<>(Arrays.asList(Long.valueOf(0)));
		PAsserts.notEmpty(val, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_notEmpty_6() {
		var val = new Stack<>();
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.notEmpty(val, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_notEmpty_6_success() {
		var val = new Stack<>();
		val.push(Long.valueOf(0));
		PAsserts.notEmpty(val, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_notEmpty_7() {
		var val = new HashedMap<>();
		assertThrows(AssertException.class, () -> {
			PAsserts.notEmpty(val);
		});
	}

	@Test
	void test_notEmpty_7_success() {
		var val = new HashedMap<>();
		val.put("1", Long.valueOf(0));
		PAsserts.notEmpty(val);
		assertTrue(true);
	}

	@Test
	void test_notEmpty_8() {
		var val = new LinkedHashMap<>();
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.notEmpty(val, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_notEmpty_8_success() {
		var val = new LinkedHashMap<>();
		val.put("1", Long.valueOf(0));
		PAsserts.notEmpty(val, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_notEmpty_9() {
		var val = new HashedMap<>(10);
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.notEmpty(val, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_notEmpty_9_success() {
		var val = new HashedMap<>(10);
		val.put("1", Long.valueOf(0));
		PAsserts.notEmpty(val, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_contains_1() {
		var val1 = "hede";
		var val2 = "wewe";
		assertThrows(AssertException.class, () -> {
			PAsserts.contains(val1, val2);
		});
	}

	@Test
	void test_contains_1_success() {
		var val1 = "hede";
		var val2 = "he";
		PAsserts.contains(val1, val2);
		assertTrue(true);
	}

	@Test
	void test_contains_2() {
		var val1 = "hede";
		var val2 = "wewe";
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.contains(val1, val2, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_contains_2_success() {
		var val1 = "hede";
		var val2 = "he";
		PAsserts.contains(val1, val2, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_contains_3() {
		var val1 = "hede";
		var val2 = "wewe";
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.contains(val1, val2, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_contains_3_success() {
		var val1 = "hede";
		var val2 = "he";
		PAsserts.contains(val1, val2, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_contains_4() {
		var val1 = new Object[] { Long.valueOf(1) };
		var val2 = "wewe";
		assertThrows(AssertException.class, () -> {
			PAsserts.contains(val1, val2);
		});
	}

	@Test
	void test_contains_4_success() {
		var val1 = new Object[] { Long.valueOf(1), Long.valueOf(1) };
		var val2 = Long.valueOf(1);
		PAsserts.contains(val1, val2);
		assertTrue(true);
	}

	@Test
	void test_contains_5() {
		var val1 = new Object[] { Long.valueOf(1) };
		var val2 = Long.valueOf(2);
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.contains(val1, val2, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_contains_5_success() {
		var val1 = new Object[] { Long.valueOf(1), Long.valueOf(1) };
		var val2 = Long.valueOf(1);
		PAsserts.contains(val1, val2, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_contains_6() {
		var val1 = new Object[] { Long.valueOf(1) };
		Object val2 = null;
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.contains(val1, val2, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_contains_6_success() {
		var val1 = new Object[] { Long.valueOf(1), Long.valueOf(1) };
		var val2 = Long.valueOf(1);
		PAsserts.contains(val1, val2, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_contains_7() {
		Collection<Object> val1 = Arrays.asList(Long.valueOf(1));
		Object val2 = "wewe";
		assertThrows(AssertException.class, () -> {
			PAsserts.contains(val1, val2);
		});
	}

	@Test
	void test_contains_7_success() {
		Collection<Object> val1 = Arrays.asList(Long.valueOf(1), Long.valueOf(1));
		var val2 = Long.valueOf(1);
		PAsserts.contains(val1, val2);
		assertTrue(true);
	}

	@Test
	void test_contains_8() {
		Collection<Object> val1 = Arrays.asList(Long.valueOf(1));
		Object val2 = Long.valueOf(2);
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.contains(val1, val2, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_contains_8_success() {
		Collection<Object> val1 = Arrays.asList(Long.valueOf(1), Long.valueOf(1));
		var val2 = Long.valueOf(1);
		PAsserts.contains(val1, val2, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_contains_9() {
		Collection<Object> val1 = Arrays.asList(Long.valueOf(1));
		Object val2 = null;
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.contains(val1, val2, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_contains_9_success() {
		Collection<Object> val1 = Arrays.asList(Long.valueOf(1), Long.valueOf(1));
		var val2 = Long.valueOf(1);
		PAsserts.contains(val1, val2, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_equals_1() {
		var val1 = "hede";
		var val2 = "HEDE";
		assertThrows(AssertException.class, () -> {
			PAsserts.equals(val1, val2);
		});
	}

	@Test
	void test_equals_1_success() {
		var val1 = "hede";
		var val2 = "hede";
		PAsserts.equals(val1, val2);
		assertTrue(true);
	}

	@Test
	void test_equals_2() {
		var val1 = "hede";
		var val2 = "";
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.equals(val1, val2, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_equals_2_success() {
		var val1 = "hede";
		var val2 = "hede";
		PAsserts.equals(val1, val2, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_equals_3() {
		var val1 = "hede";
		String val2 = null;
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.equals(val1, val2, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_equals_3_success() {
		var val1 = "hede";
		var val2 = "hede";
		PAsserts.equals(val1, val2, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_equals_object_1() {
		Object val1 = Long.valueOf(1);
		Object val2 = "wewe";
		assertThrows(AssertException.class, () -> {
			PAsserts.equals(val1, val2);
		});
	}

	@Test
	void test_equals_object_1_success() {
		Object val1 = Long.valueOf(1);
		Object val2 = Long.valueOf(1);
		PAsserts.equals(val1, val2);
		assertTrue(true);
	}

	@Test
	void test_equals_object_2() {
		Object val1 = Long.valueOf(1);
		Object val2 = Long.valueOf(2);
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.equals(val1, val2, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_equals_object_2_success() {
		Object val1 = Long.valueOf(1);
		Object val2 = Long.valueOf(1);
		PAsserts.equals(val1, val2, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_equals_object_3() {
		Object val1 = "hede";
		Object val2 = null;
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.equals(val1, val2, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_equals_object_3_success() {
		Object val1 = Long.valueOf(1);
		Object val2 = Long.valueOf(1);
		PAsserts.equals(val1, val2, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_true_1() {
		assertThrows(AssertException.class, () -> {
			PAsserts.isTrue(false);
		});
	}

	@Test
	void test_true_1_success() {
		PAsserts.isTrue(true);
		assertTrue(true);
	}

	@Test
	void test_true_2() {
		assertThrows(AssertException.class, () -> {
			PAsserts.isTrue(Boolean.FALSE);
		});
	}

	@Test
	void test_true_2_success() {
		PAsserts.isTrue(Boolean.TRUE);
		assertTrue(true);
	}

	@Test
	void test_true_3() {
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.isTrue(false, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_true_3_success() {
		PAsserts.isTrue(true, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_true_4() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.isTrue(Boolean.FALSE, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_true_4_success() {
		PAsserts.isTrue(Boolean.TRUE, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_true_5() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.isTrue(false, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_true_6() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.isTrue(Boolean.FALSE, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_false_1() {
		assertThrows(AssertException.class, () -> {
			PAsserts.isFalse(true);
		});
	}

	@Test
	void test_false_2() {
		assertThrows(AssertException.class, () -> {
			PAsserts.isFalse(Boolean.TRUE);
		});
	}

	@Test
	void test_false_3() {
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.isFalse(true, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_false_4() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.isFalse(Boolean.TRUE, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_false_5() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.isFalse(true, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_false_6() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.isFalse(Boolean.TRUE, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_not_null_1() {
		assertThrows(AssertException.class, () -> {
			PAsserts.notNull(null);
		});
	}

	@Test
	void test_not_null_2() {
		var exception = assertThrows(AssertException.class, () -> {
			PAsserts.notNull(null, () -> "hedeleme");
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_not_null_3() {
		var exception = assertThrows(TestException.class, () -> {
			PAsserts.notNull(null, () -> "hedeleme", () -> TestException.class);
		});
		assertEquals("hedeleme", exception.getMessage());
	}

	@Test
	void test_true_5_success() {
		PAsserts.isTrue(true, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_true_6_success() {
		PAsserts.isTrue(Boolean.TRUE, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_false_1_success() {
		PAsserts.isFalse(false);
		assertTrue(true);
	}

	@Test
	void test_false_2_success() {
		PAsserts.isFalse(Boolean.FALSE);
		assertTrue(true);
	}

	@Test
	void test_false_3_success() {
		PAsserts.isFalse(false, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_false_4_success() {
		PAsserts.isFalse(Boolean.FALSE, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_false_5_success() {
		PAsserts.isFalse(false, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_false_6_success() {
		PAsserts.isFalse(Boolean.FALSE, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_not_null_1_success() {
		PAsserts.notNull(Long.valueOf(1));
		assertTrue(true);
	}

	@Test
	void test_not_null_2_success() {
		PAsserts.notNull(Long.valueOf(0), () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_not_null_3_success() {
		PAsserts.notNull(Long.valueOf(0), () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

	@Test
	void test_is_null_1_success() {
		PAsserts.isNull(null);
		assertTrue(true);
	}

	@Test
	void test_is_null_2_success() {
		PAsserts.isNull(null, () -> "hedeleme");
		assertTrue(true);
	}

	@Test
	void test_is_null_3_success() {
		PAsserts.isNull(null, () -> "hedeleme", () -> TestException.class);
		assertTrue(true);
	}

}
