package com.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.service.UserPrincipalService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			// Anything which is not the default page => must be authenticated
			.antMatchers(HttpMethod.GET, "/js/**", "/css/**", "/img/**").permitAll()
			.antMatchers(HttpMethod.POST, "/api/v1/user", "/authenticate").permitAll()
			.antMatchers(HttpMethod.GET, "/api/**").permitAll()
			.antMatchers(HttpMethod.POST, "/api/**").permitAll()
			.antMatchers(HttpMethod.PUT, "/api/**").permitAll()
			.antMatchers(HttpMethod.DELETE, "/api/**").permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
			.antMatchers("/", "/home", "/index.html").permitAll()
			.anyRequest().authenticated()
			.and()
			// Login page => freely available
			.formLogin().loginPage("/login-form").permitAll()
			.and()
			// Logout page => freely available
			.logout().permitAll();

		// CRCF: Cross-site request forgery
		// Needs to be disabled to access from another Web server and mobile applications
		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
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
}
