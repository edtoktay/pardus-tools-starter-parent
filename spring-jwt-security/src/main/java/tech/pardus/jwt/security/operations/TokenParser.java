/**
 *
 */
package tech.pardus.jwt.security.operations;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import tech.pardus.jwt.security.exceptions.TokenMissingException;
import tech.pardus.jwt.security.properties.JwtProperties;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
@Component
public class TokenParser {

	@Resource
	private HttpServletRequest request;

	@Autowired
	private JwtProperties jwtProperties;

	@SuppressWarnings("unchecked")
	public UserDetails parseClaims(String token) {
		// @formatter:off
		var claims = Jwts
						.parser()
							.setSigningKey(jwtProperties.getSignKey())
						.parseClaimsJws(token);
		var user = new User(
					claims.getBody().getSubject(),
					"",
					(Collection<? extends GrantedAuthority>)
						claims
							.getBody()
								.get("roles", List.class)
									.stream()
										.map(auth -> new SimpleGrantedAuthority((String) auth))
									.collect(Collectors.toList()));
		SecurityContextHolder
			.getContext()
				.setAuthentication(
						new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		return user;
		// @formatter:on
	}

	public UserDetails parseClaims() {
		if (hasToken()) {
			return parseClaims(getToken());
		}
		throw new TokenMissingException("token_not_presented");
	}

	public boolean hasToken() {
		return StringUtils.isNotBlank(getToken());
	}

	public String getToken() {
		var requestTokenHeader = request.getHeader(jwtProperties.getHeader());
		if (requestTokenHeader != null && requestTokenHeader.startsWith(jwtProperties.getHeaderPrefix())) {
			var jwtToken = requestTokenHeader.substring(jwtProperties.getHeaderPrefix().strip().length() + 1);
			return jwtToken;
		}
		return null;
	}

}
