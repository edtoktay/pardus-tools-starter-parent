# Spring JWT Server -Client Handler #

Handles JWT based authorization both Server-Side and Client-Side

### Requirements ###
* JDK 11 or higher
* Maven 3.6 or higher (build only)
* spring-boot-dependencies 2.6.0

### Installation ###
```xml
<dependency>
	<groupId>tech.pardus</groupId>
	<artifactId>spring-jwt-security</artifactId>
	<version>${pardus.tools.versions}</version>
</dependency>
  ```

### Usage ###
#### Server Side ####
##### application.yml #####
```yaml
security:
  jwt:
    token:
      issuer: true # default is false
      expiration-time: 30 # Duration amount of token expiry
      expiry-time-unit: SECONDS #SECONDS, MINUTES, HOURS, DAYS default is MINUTES
      issuing-authority: pardus.tech # name of the token issuing authority
      sign-key: any-sign-key # HS512 Signature secret key
      header: Authorization # header field default is Authorization
      header-prefix: Bearer # Header Prefix for token default is Bearer 
      login-url: /authentication/login # login end point URL
```
application.yml without default values (10 Minutes Expiry Time)
```Yaml
security:
  jwt:
    token:
      issuer: true
      expiration-time: 10
      issuing-authority: pardus.tech
      sign-key: any-sign-key
      login-url: /authentication/login
```
##### AuthenticationProvider #####
Server side requires to implement ```org.springframework.security.authentication.AuthenticationProvider ```

###### Sample Authentication Provider ######
```java
@Component
@Primary
public class IssuerAuthenticationProvider implements AuthenticationProvider {

  // @formatter:off
  private static List<TestUser> users =
      Arrays.asList(
          new TestUser(
              "Test1",
              "1234",
              new String[] {"ADMIN", "AGENT"},
              new String[] {"test_key_1", "test_key_2"}),
          new TestUser(
              "Test2", "5678", new String[] {"USER"}, new String[] {"test_key_1", "test_key_3"}));
  // @formatter:on

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    Object credentials = authentication.getCredentials();
    System.out.println("credentials class: " + credentials.getClass());
    if (!(credentials instanceof String)) {
      return null;
    }
    String password = credentials.toString();
    Optional<TestUser> userOptional =
        users.stream().filter(u -> u.match(name, password)).findFirst();
    if (!userOptional.isPresent()) {
      throw new BadCredentialsException("Authentication failed for " + name);
    }
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.addAll(
        Stream.of(userOptional.get().getAccessKeys())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()));
    grantedAuthorities.addAll(
        Stream.of(userOptional.get().getRoles())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()));
    Authentication auth =
        new UsernamePasswordAuthenticationToken(name, password, grantedAuthorities);
    return auth;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class TestUser {

    private String name;

    private String password;

    private String[] roles;

    private String[] accessKeys;

    public boolean match(String name, String password) {
      return this.name.equals(name) && this.password.equals(password);
    }
  }
```
###### Sample Authentication Controller ######
```java
@RestController
public class AuthenticationRestService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenCreator tokenCreator;

  @PostMapping(path = "/authentication/login")
  public JwtToken login(@RequestParam("username") String username,
      @RequestParam("password") String password) {
    var auth = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    return tokenCreator.createToken(auth);
  }
}
```
No other configurations required
#### Client Side ####
##### application.yml #####
```yaml
security:
  jwt:
    token:
	  issuer: false # default is false
      sign-key: any-sign-key # HS512 Signature secret key
      header: Authorization # header field default is Authorization
      header-prefix: Bearer # Header Prefix for token default is Bearer 
```
application.yml without default values
```yaml
security:
  jwt:
    token:
      sign-key: hedeleme
```
##### Sample Rest Controller #####
```java
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
```
No other configurations required.

### Future Developments ###
*	Use NimbusJWT
*	Obtain secret-key from server
*	Collect and persists url, access-keys and roles for each client end points