package bap.jp.mvcbap.security;

import bap.jp.mvcbap.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userDetailsService;

    public SecurityConfig(UserService userDetailsService) {
	this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
			.requestMatchers("/cart/**").hasAnyRole("USER")
			.requestMatchers("payment/**").hasAnyRole("USER")
			.requestMatchers("/orders/**").hasAnyRole("USER", "ADMIN")
			.requestMatchers("/users/**").hasAnyRole("ADMIN")
			.requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
			.anyRequest().authenticated()
		)
		.formLogin(form -> form
			.loginPage("/login")
			.defaultSuccessUrl("/", true)
			.permitAll()
		)
		.logout(logout -> logout
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		);

	return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
	    AuthenticationConfiguration config) throws Exception {
	return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	authProvider.setUserDetailsService(userDetailsService);
	authProvider.setPasswordEncoder(passwordEncoder());
	return authProvider;
    }
}
