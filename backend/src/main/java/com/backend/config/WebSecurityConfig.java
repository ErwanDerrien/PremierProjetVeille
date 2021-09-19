package com.backend.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.backend.controller.security.JwtRequestFilter;
import com.backend.service.UserPrincipalService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	JwtRequestFilter requestFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Set session management to stateless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Reject unauthenticated request and send status 401
		http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		});

		// Authorize some requests
		http.authorizeRequests()
				// Homepage is freely available
				.antMatchers("/", "/home", "/index.html").permitAll()
				// Static resources are freely available
				.antMatchers(HttpMethod.GET, "/js/**", "/css/**", "/img/**", "/favicon.ico").permitAll()
				// Sign-in and sign-up endpoints are freely available
				.antMatchers(HttpMethod.GET, "/api/v1/activate").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/user", "/api/v1/authenticate", "/api/v1/resetpsw").permitAll()
				// Options request for CORS are freely available
				.antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
				// Reject all other requests
				.anyRequest().authenticated();

		// Add JWT token filter to validate every requests
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

		// CORS: Cross-origin resource sharing -- Enables it to accept REST API calls
		// from other domains
		// CRCF: Cross-site request forgery -- Needs to be disabled to access from
		// another Web server and mobile applications
		http.cors().and().csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return new UserPrincipalService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
