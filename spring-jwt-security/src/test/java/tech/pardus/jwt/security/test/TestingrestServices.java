/** */
package tech.pardus.jwt.security.test;

import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.pardus.jwt.security.annotation.SecuredEndPoint;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@RestController
@RequestMapping("/test")
public class TestingrestServices {

  @GetMapping(path = "/test-auth-only/{value}")
  @SecuredEndPoint
  public String testAuthedOnly(@PathVariable("value") String[] val) {
    var sb = new StringBuilder();
    Stream.of(val).forEach(sb::append);
    sb.append("-OK");
    return sb.toString();
  }

  @PostMapping(path = "/test-single-role")
  @SecuredEndPoint(roles = {"AGENT"})
  public String testSingleRole(@RequestParam("value") String val) {
    return val + "-OK";
  }

  @PostMapping(path = "/test-double-role")
  @SecuredEndPoint(roles = {"AGENT", "USER"})
  public String testDoubleRole(@RequestParam("value") String val) {
    return val + "-OK";
  }

  @PostMapping(path = "/test-access-key")
  @SecuredEndPoint(accessKey = "test_key_1")
  public String testAccesKey(@RequestBody TestObj val) {
    return val.getVal() + "-OK";
  }

  @PostMapping(path = "/test-access-or-role")
  @SecuredEndPoint(
      accessKey = "test_key_2",
      roles = {"USER"})
  public String testAccesKeyOrRole(@RequestBody TestObj val) {
    return val.getVal() + "-OK";
  }
}
