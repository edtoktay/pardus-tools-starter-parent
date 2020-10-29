/**
 *
 */
package tech.pardus.jwt.security.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
public class RequestPathsMatcher implements RequestMatcher {

	private OrRequestMatcher publicMatchers;

	private OrRequestMatcher privateMatchers;

	public RequestPathsMatcher(List<String> publicRequests, List<String> privateRequests) {
		if (CollectionUtils.isEmpty(publicRequests)) {
			publicRequests = new ArrayList<>();
		}
		List<RequestMatcher> publics = publicRequests.stream().map(url -> new AntPathRequestMatcher(url))
		        .collect(Collectors.toList());
		if (CollectionUtils.isEmpty(privateRequests)) {
			privateRequests = new ArrayList<>();
		}
		List<RequestMatcher> privates = privateRequests.stream().map(url -> new AntPathRequestMatcher(url))
		        .collect(Collectors.toList());
		this.publicMatchers = new OrRequestMatcher(publics);
		this.privateMatchers = new OrRequestMatcher(privates);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return publicMatchers.matches(request) ? false : privateMatchers.matches(request) ? true : false;
	}

}
