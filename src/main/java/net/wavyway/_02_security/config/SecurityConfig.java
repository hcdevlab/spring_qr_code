
package net.wavyway._02_security.config;

import net.wavyway._02_security.oauth2.JwtAuthorizationFilter;
import net.wavyway._02_security.oauth2.JwtService;
import net.wavyway._02_security.oauth2.LogInAuthenticationFilter;
import net.wavyway._02_security.oauth2.LogoutPathHandler;
import net.wavyway._02_security.user.IUserBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("dataSourceSecurity")
	private DataSource dataSourceSecurity = null;

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		super.configure(authenticationManagerBuilder);
		authenticationManagerBuilder
				.jdbcAuthentication()
				.dataSource(dataSourceSecurity)
				.passwordEncoder(passwordEncoder())
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public static CorsConfigurationSource corsConfigurationSource() {
		List<String> exposedHeaders = Collections.singletonList("Authorization");
		List<String> allowedOrigins = Collections.singletonList("*");
		List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE");
		List<String> allowedHeaders = Arrays.asList("Authorization",
			"Cache-Control",
			"Content-Type",
			"X-Requested-With",
			"Origin",
			"Accept");

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setExposedHeaders(exposedHeaders);
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.setAllowedMethods(allowedMethods);
		configuration.setAllowedHeaders(allowedHeaders);
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Configuration
	@Order(1)
	public static class MobileSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		private final IUserBinder userBinder;
		private JwtService jwtService;

		public MobileSecurityConfigurerAdapter(@Autowired @Qualifier("userBinder") IUserBinder userBinder,
		                                       @Autowired @Qualifier("jwtService") JwtService jwtService) {
			super(true);
			this.userBinder = userBinder;
			this.jwtService = jwtService;
		}

		private AuthenticationEntryPointConfiguration authenticationEntryPointConfiguration;

		@Autowired
		public void setCustomAuthenticationEntryPoint(AuthenticationEntryPointConfiguration authenticationEntryPointConfiguration) {
			this.authenticationEntryPointConfiguration = authenticationEntryPointConfiguration;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.cors().configurationSource(corsConfigurationSource());
			http.headers().defaultsDisabled().cacheControl();
			http.antMatcher("/mobile/**").authorizeRequests();
			http.csrf().disable().exceptionHandling();//.authenticationEntryPoint(authenticationEntryPointConfiguration);
			http.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/mobile/secpath").permitAll()
					.antMatchers("/mobile/**").authenticated();
			http
					.addFilter(new LogInAuthenticationFilter(authenticationManager(), jwtService))
					.addFilter(new JwtAuthorizationFilter(authenticationManager(), userBinder, jwtService));
		}
	}

	@Configuration
	@Order(2)
	public static class WebSecurityConfigurationFilterAdapter extends WebSecurityConfigurerAdapter {
		private final IUserBinder userBinder;
		private JwtService jwtService;

		public WebSecurityConfigurationFilterAdapter(@Autowired @Qualifier("userBinder") IUserBinder userBinder,
		                                             @Autowired @Qualifier("jwtService") JwtService jwtService) {
			this.userBinder = userBinder;
			this.jwtService = jwtService;
		}

		@Bean
		public AuthenticationFailureHandler authenticationFailureHandler() {
			return new AccessTokenAuthenticationFailureHandler();
		}

		private LogoutPathHandler logoutPathHandler;

		@Autowired
		public void setLogoutPathHandler(LogoutPathHandler logoutPathHandler) {
			this.logoutPathHandler = logoutPathHandler;
		}

		private AuthenticationEntryPointConfiguration authenticationEntryPointConfiguration;

		@Autowired
		public void setCustomAuthenticationEntryPoint(AuthenticationEntryPointConfiguration authenticationEntryPointConfiguration) {
			this.authenticationEntryPointConfiguration = authenticationEntryPointConfiguration;
		}

		private AccessDeniedHandlerConfig accessDeniedHandlerConfig;

		@Autowired
		public void setCustomAccessDeniedHandler(AccessDeniedHandlerConfig accessDeniedHandlerConfig) {
			this.accessDeniedHandlerConfig = accessDeniedHandlerConfig;
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			super.configure(web);
			web
					.ignoring()
					.antMatchers("/css/**", "/js/**", "/img/**", "/error/**");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.cors().configurationSource(corsConfigurationSource());
			http.headers().defaultsDisabled().cacheControl();
			http.csrf();
			http.authorizeRequests().antMatchers(HttpMethod.POST, "/secpath").permitAll()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.antMatchers("/", "/index").permitAll()
					.anyRequest().authenticated()
					.and()
					.addFilter(new LogInAuthenticationFilter(authenticationManager(), jwtService))
					.addFilter(new JwtAuthorizationFilter(authenticationManager(), userBinder, jwtService))
					.exceptionHandling().authenticationEntryPoint(authenticationEntryPointConfiguration)
					.and().exceptionHandling().accessDeniedHandler(accessDeniedHandlerConfig)
					.accessDeniedPage("/error")
					.and()
					.logout()
					.logoutUrl("/private/log_out")
					.logoutSuccessUrl("/index")
					.clearAuthentication(true)
					.deleteCookies("JSESSIONID", "Authorization");
		}
	}
}
