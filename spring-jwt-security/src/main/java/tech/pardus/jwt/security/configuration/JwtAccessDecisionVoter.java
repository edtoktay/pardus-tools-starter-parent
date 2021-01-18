/**
 *
 */
package tech.pardus.jwt.security.configuration;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.bind.annotation.RequestMethod;

import tech.pardus.jwt.security.annotation.impl.ModuleUrlSecurityRegistry;
import tech.pardus.jwt.security.operations.TokenParser;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
public class JwtAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
		var registry = ModuleUrlSecurityRegistry.registry();
		var tokenConsumer = WebSecuritySpringContext.getBean(TokenParser.class);
		var url = object.getRequestUrl();
		var method = RequestMethod.valueOf(object.getRequest().getMethod());
		if (registry.isPublicUrl(method, url)) {
			return ACCESS_GRANTED;
		}
		if (tokenConsumer.hasToken()) {
			var user = tokenConsumer.parseClaims();
			return registry.hasRight(user, url, method);
		}
		return ACCESS_DENIED;
	}

}
