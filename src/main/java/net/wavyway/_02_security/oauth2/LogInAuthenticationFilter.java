package net.wavyway._02_security.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import static net.wavyway._02_security.oauth2.SecurityConstants.*;

public class LogInAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private JwtService jwtService;

	public LogInAuthenticationFilter(AuthenticationManager authenticationManager,
	                                 @Autowired @Qualifier("jwtService") JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
		setPostOnly(true);
		setAuthenticationManager(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		HashMap<String, String> map = new HashMap<String, String>();
		try {

			BufferedReader reader = request.getReader();
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
			map = mapper.readValue(reader, typeRef);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		String userName;
		String password;

		if ((map.get("username") != null) && (!map.get("username").isEmpty())) {
			userName = map.get("username");
		} else {
			userName = "";
		}

		if ((map.get("password") != null) && (!map.get("password").isEmpty())) {
			password = map.get("password");
		} else {
			password = "";
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authentication)
			throws IOException, ServletException {

		User user = (User) authentication.getPrincipal();
		String userName = user.getUsername();
		String tokenValue = jwtService.generateJWTToken(userName);

		Cookie cookie = new Cookie(TOKEN_HEADER, tokenValue);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
	                                          HttpServletResponse response,
	                                          AuthenticationException failed)
			throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
