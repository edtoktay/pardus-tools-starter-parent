package tech.pardus.utilities.test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static tech.pardus.utilities.ReflectionUtils.getMainClass;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Reflection Utils Test Cases
 *
 * @author deniz.toktay
 * @since Aug 20, 2020
 */
@TestMethodOrder(OrderAnnotation.class)
class ReflectionUtilsTest {

  @Test
  @Order(1)
  void test_can_not_find_main() throws ClassNotFoundException {
    var mainClass = getMainClass();
    assertNull(mainClass);
  }
}
