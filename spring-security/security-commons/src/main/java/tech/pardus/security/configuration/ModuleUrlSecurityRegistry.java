/**
 *
 */
package tech.pardus.security.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.pardus.security.exceptions.UnknownEndPointException;
import tech.pardus.security.model.EndPointModel;

/**
 * @author edtoktay
 * @since Dec 20, 2021
 */
public class ModuleUrlSecurityRegistry {
  private final int ACCESS_GRANTED = 1;

  private final int ACCESS_DENIED = -1;

  private static ModuleUrlSecurityRegistry instance = null;

  private HashMap<RequestMethod, Set<EndPointModel>> urlRegistry;

  private ModuleUrlSecurityRegistry() {
    urlRegistry = new HashMap<>();
  }

  public static ModuleUrlSecurityRegistry registry() {
    if (instance == null) {
      synchronized (ModuleUrlSecurityRegistry.class) {
        if (instance == null) {
          instance = new ModuleUrlSecurityRegistry();
        }
      }
    }
    return instance;
  }

  public void registerNewUrl(RequestMethod method, String url, String privilege, List<String> roles,
      boolean isSecured) {
    if (urlRegistry == null) {
      urlRegistry = new HashMap<>();
    }
    if (!urlRegistry.containsKey(method)) {
      urlRegistry.put(method, new HashSet<>());
    }
    var endPointBuilder = EndPointModel.builder();
    endPointBuilder.url(url).accessKey(privilege);
    if (CollectionUtils.isNotEmpty(roles)) {
      roles.stream().forEach(role -> endPointBuilder.addRole(role));
    }
    if (isSecured) {
      endPointBuilder.isSecured();
    }
    urlRegistry.get(method).add(endPointBuilder.build());
  }

  public int hasRight(UserDetails user, String url, RequestMethod method) {
    // @formatter:off
    if (isPublicUrl(method, url)) {
      return ACCESS_GRANTED;
    }
    var endPointModel = getEndPoint(method, url);
    Predicate<GrantedAuthority> urlModelPredicate =
        authority -> endPointModel.eligibleAuthority(authority);
    return user.getAuthorities()
        .stream()
        .filter(urlModelPredicate)
        .findFirst()
        .map(t -> ACCESS_GRANTED)
        .orElse(ACCESS_DENIED);
    // @formatter:on
  }

  public boolean isSecuredUrl(RequestMethod method, String url) {
    return Objects.nonNull(getEndPoint(method, url))
        && getEndPoint(method, url).isSecuredEndPoint();
  }

  public boolean isPublicUrl(RequestMethod method, String url) {
    return !isSecuredUrl(method, url);
  }

  private EndPointModel getEndPoint(RequestMethod method, String url) {
    Predicate<EndPointModel> predicate = t -> t.isUrlMatched(url);
    return CollectionUtils.isEmpty(urlRegistry.get(method)) ? null
        : urlRegistry.get(method).stream().filter(predicate).findFirst()
            .orElseThrow(UnknownEndPointException::new);
  }
}
