package net.wavyway._02_security.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointConfiguration implements AuthenticationEntryPoint {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException)
			throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no permitido. Debe estar autenticado.");
	}
}
