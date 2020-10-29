/**
 *
 */
package tech.pardus.jwt.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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

import tech.pardus.jwt.security.SpringSecurityConfiguration;
import tech.pardus.jwt.security.operations.TokenCreator.JwtToken;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@SpringBootTest(classes = { App.class, SpringSecurityConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("issuer")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtIssuerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	private static JwtToken tokenObject;

	@LocalServerPort
	private int port;

	// @formatter:off
	@Test
	@Order(1)
	void test_login() throws URISyntaxException {
		var uri = getUrl("/authentication/login");
		var builder = UriComponentsBuilder
				.fromUri(uri)
					.queryParam("username", "Test1")
					.queryParam("password", "1234");
		var authResponse = testRestTemplate.postForEntity(builder.build().toUri(), null, JwtToken.class);
		tokenObject = authResponse.getBody();
		assertNotNull(authResponse);
		assertEquals("Authorization", authResponse.getBody().getHeader());
	}

	@Test
	@Order(2)
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
	@Order(2)
	void test_single_role() throws URISyntaxException {
		var uri = getUrl("/test/test-single-role");
		var builder = UriComponentsBuilder
				.fromUri(uri)
					.queryParam("value", "test");
		var httpEntity = new HttpEntity<>(getHeaders());
		var response = testRestTemplate.exchange(builder.build().toUri(), HttpMethod.POST, httpEntity, String.class);
		assertEquals("test-OK", response.getBody());
	}

	@Test
	@Order(2)
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
	@Order(2)
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
	@Order(2)
	void test_access_key_or_role() throws URISyntaxException{
		var uri = getUrl("/test/test-access-or-role");
		var header = getHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		var testObj = new TestObj("test");
		var httpEntity = new HttpEntity<>(testObj, header);
		var response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
		assertEquals("test-OK", response.getBody());
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
