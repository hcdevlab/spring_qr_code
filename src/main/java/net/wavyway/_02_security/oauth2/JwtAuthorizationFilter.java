package net.wavyway._02_security.oauth2;

import net.wavyway._02_security.user.IUserBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	private final IUserBinder userBinder;
	private JwtService jwtService;

	public JwtAuthorizationFilter(
			AuthenticationManager authenticationManager,
			@Autowired @Qualifier("userBinder") IUserBinder userBinder,
			@Autowired @Qualifier("jwtService") JwtService jwtService) {

		super(authenticationManager);
		this.userBinder = userBinder;
		this.jwtService = jwtService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request,
	                             HttpServletResponse response,
	                             FilterChain filterChain) throws IOException, ServletException {

		String token = jwtService.getAuthorizationCookieFromRequest(request);

		if ((token == null) || (!token.startsWith("Bearer "))) {
			filterChain.doFilter(request, response);
			return;
		} else {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
			if (authentication == null) {
				filterChain.doFilter(request, response);
				return;
			} else {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			}
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest httpServletRequest) {
		String token = jwtService.getAuthorizationCookieFromRequest(httpServletRequest);

		if (token != null) {
			String user = jwtService.getUserAfterVerifyJwtToken(token);

			if (!user.equals("empty")) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}

			return null;
		}
		return null;
	}
}
