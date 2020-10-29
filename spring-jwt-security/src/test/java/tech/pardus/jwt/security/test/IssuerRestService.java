/**
 *
 */
package tech.pardus.jwt.security.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.pardus.jwt.security.operations.TokenCreator;
import tech.pardus.jwt.security.operations.TokenCreator.JwtToken;

/**
 * @author deniz.toktay
 * @since Oct 29, 2020
 */
@RestController
@Profile("issuer")
public class IssuerRestService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenCreator tokenCreator;

	@PostMapping(path = "/authentication/login")
	public JwtToken login(@RequestParam("username") String username, @RequestParam("password") String password) {
		var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		return tokenCreator.createToken(auth);
	}

}
