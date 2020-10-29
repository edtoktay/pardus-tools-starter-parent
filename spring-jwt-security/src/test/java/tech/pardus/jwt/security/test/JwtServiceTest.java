/**
 *
 */
package tech.pardus.jwt.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tech.pardus.jwt.security.SpringSecurityConfiguration;
import tech.pardus.jwt.security.operations.TokenCreator.JwtToken;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@SpringBootTest(classes = { App.class, SpringSecurityConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JwtServiceTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	private static JwtToken tokenObject;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void init() {
		var claims = Jwts.claims().setSubject("Test2");
		claims.put("roles", Arrays.asList("USER", "test_key_1", "test_key_3"));
		var now = LocalDateTime.now();
		var issueStartDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		var expiryDate = now.plus(30, ChronoUnit.SECONDS);
		var issueExpiryDate = Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant());
		var token = Jwts.builder().setClaims(claims).setIssuer("pardus.tech").setIssuedAt(issueStartDate)
		        .setExpiration(issueExpiryDate).signWith(SignatureAlgorithm.HS512, "hedeleme").compact();
		tokenObject = JwtToken.builder().token(token).header("Authorization").headerPrefix("Bearer")
		        .expireTime(expiryDate).build();
	}
	// @formatter:off

	@Test
	void test_authed() throws URISyntaxException {
		var uri = getUrl("/test/test-auth-only");
		var builder = UriComponentsBuilder
				.fromUri(uri)
					.path("/test,xer,where");
		var httpEntity = new HttpEntity<>(getHeaders());
		var response = testRestTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
		assertEquals("testxerwhere-OK", response.getBody());
	}

	@Test
	void test_single_role() throws URISyntaxException {
		var uri = getUrl("/test/test-single-role");
		var builder = UriComponentsBuilder
				.fromUri(uri)
					.queryParam("value", "test");
		var httpEntity = new HttpEntity<>(getHeaders());
		var response = testRestTemplate.exchange(builder.build().toUri(), HttpMethod.POST, httpEntity, String.class);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

	@Test
	void test_double_role() throws URISyntaxException{
		var uri = getUrl("/test/test-double-role");
		var builder = UriComponentsBuilder
				.fromUri(uri)
					.queryParam("value", "test");
		var httpEntity = new HttpEntity<>(getHeaders());
		var response = testRestTemplate.exchange(builder.build().toUri(), HttpMethod.POST, httpEntity, String.class);
		assertEquals("test-OK", response.getBody());
	}

	@Test
	void test_access_key() throws URISyntaxException{
		var uri = getUrl("/test/test-access-key");
		var header = getHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		var testObj = new TestObj("test");
		var httpEntity = new HttpEntity<>(testObj, header);
		var response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
		assertEquals("test-OK", response.getBody());
	}

	@Test
	void test_access_key_or_role() throws URISyntaxException{
		var uri = getUrl("/test/test-access-or-role");
		var header = getHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		var testObj = new TestObj("test");
		var httpEntity = new HttpEntity<>(testObj, header);
		var response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
		assertEquals("test-OK", response.getBody());
	}

	@Test
	void test_expiry() throws URISyntaxException, InterruptedException{
		var uri = getUrl("/test/test-single-role");
		var builder = UriComponentsBuilder
				.fromUri(uri)
					.queryParam("value", "test");
		var httpEntity = new HttpEntity<>(getHeaders());
		var latch =  new CountDownLatch(1);
		latch.await(31, TimeUnit.SECONDS);
		var response = testRestTemplate.exchange(builder.build().toUri(), HttpMethod.POST, httpEntity, String.class);
		assertTrue(response.getStatusCode().is5xxServerError());
	}

	private HttpHeaders getHeaders() {
		var headers = new HttpHeaders();
		headers.set(tokenObject.getHeader(), tokenObject.getHeaderPrefix() + " " + tokenObject.getToken());
		return headers;
	}
	// @formatter:on

	private URI getUrl(String extension) throws URISyntaxException {
		var baseUrl = "http://localhost:" + port + extension;
		return new URI(baseUrl);
	}

}
