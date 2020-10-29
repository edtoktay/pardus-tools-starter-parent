/**
 *
 */
package tech.pardus.jwt.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Oct 28, 2020
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EndPointModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private boolean usedUrlParams;

	@EqualsAndHashCode.Include
	private String url;

	private String accessKey;

	private Set<String> roles = new HashSet<>();

	private boolean securedEndPoint = false;

	public static Builder builder() {
		return new EndPointModel.Builder();
	}

	public boolean isUrlMatched(String url2Check) {
		if (usedUrlParams) {
			var matcher = Pattern.compile(url).matcher(url2Check);
			return matcher.find();
		}
		if (StringUtils.contains(url2Check, "?")) {
			url2Check = StringUtils.substring(url2Check, 0, StringUtils.indexOfIgnoreCase(url2Check, "?"));
		}
		return StringUtils.equals(url, url2Check);
	}

	public boolean eligibleAuthority(GrantedAuthority authority) {
		var tokenOnly = StringUtils.isBlank(accessKey) && roles.isEmpty();
		return tokenOnly ? tokenOnly
		        : roles.contains(authority.toString()) || StringUtils.equals(accessKey, authority.toString());
	}

	/**
	 * @author deniz.toktay
	 * @since Oct 28, 2020
	 */
	@NoArgsConstructor
	public static class Builder {

		private final EndPointModel managedInstance = new EndPointModel();

		public Builder url(String url) {
			if (StringUtils.contains(url, "{")) {
				managedInstance.url = findRegexedUrl(url);
				managedInstance.usedUrlParams = true;
			} else {
				managedInstance.url = url;
				managedInstance.usedUrlParams = false;
			}
			return this;
		}

		public Builder accessKey(String accessKey) {
			managedInstance.accessKey = accessKey;
			return this;
		}

		public Builder isSecured() {
			managedInstance.securedEndPoint = true;
			return this;
		}

		public Builder addRole(String role) {
			if (CollectionUtils.isEmpty(managedInstance.roles)) {
				managedInstance.roles = new HashSet<>();
			}
			managedInstance.roles.add(role);
			return this;
		}

		public EndPointModel build() {
			return managedInstance;
		}

		private String findRegexedUrl(String url2Parse) {
			var builder = new StringBuilder();
			var insertToBuilder = true;
			for (var ch : url2Parse.toCharArray()) {
				if (ch == '{') {
					insertToBuilder = false;
					builder.append("[a-zA-Z0-9]+");
				} else if (ch == '}') {
					insertToBuilder = true;
				} else if (insertToBuilder) {
					builder.append(ch);
				}
			}
			return builder.toString();
		}

	}

}
