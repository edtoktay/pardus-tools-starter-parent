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
import tech.pardus.security.annotation.SecuredEndPoint;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@RestController
@RequestMapping("/test")
public class TestingrestServices {

  /*
   * Public URL without annotation
   */
  @GetMapping(path = "/public1/{value}")
  public String testPublic1(@PathVariable("value") String[] val) {
    var sb = new StringBuilder();
    Stream.of(val).forEach(sb::append);
    sb.append("-OK");
    return sb.toString();
  }

  /*
   * Public URL with annotation, publicUrl flag set true, default is false
   */
  @GetMapping(path = "/public2")
  @SecuredEndPoint(publicUrl = true)
  public String testPublic2() {
    return "OK";
  }

  /*
   * All requests with valid JWT can access
   */
  @GetMapping(path = "/test-auth-only/{value}")
  @SecuredEndPoint
  public String testAuthedOnly(@PathVariable("value") String[] val) {
    var sb = new StringBuilder();
    Stream.of(val).forEach(sb::append);
    sb.append("-OK");
    return sb.toString();
  }

  /*
   * Only request with valid JWT, which has AGENT role ,can access
   */
  @PostMapping(path = "/test-single-role")
  @SecuredEndPoint(roles = {"AGENT"}, publicUrl = false)
  public String testSingleRole(@RequestParam("value") String val) {
    return val + "-OK";
  }

  /*
   * Only request with valid JWT, which has either AGENT or USER role, can access
   */
  @PostMapping(path = "/test-double-role")
  @SecuredEndPoint(roles = {"AGENT", "USER"})
  public String testDoubleRole(@RequestParam("value") String val) {
    return val + "-OK";
  }

  /*
   * Only request with valid JWT, which has test_key_1 access-key, can access
   */
  @PostMapping(path = "/test-access-key")
  @SecuredEndPoint(accessKey = "test_key_1")
  public String testAccesKey(@RequestBody TestObj val) {
    return val.getVal() + "-OK";
  }

  /*
   * Only request with valid JWT, which has test_key_2 access-key and/or USER role, can access
   */
  @PostMapping(path = "/test-access-or-role")
  @SecuredEndPoint(accessKey = "test_key_2", roles = {"USER"})
  public String testAccesKeyOrRole(@RequestBody TestObj val) {
    return val.getVal() + "-OK";
  }
}
