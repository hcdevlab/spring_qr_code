package net.wavyway._02_security.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogoutPathHandler implements LogoutHandler {
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		authentication.setAuthenticated(false);
		SecurityContextHolder.clearContext();
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
