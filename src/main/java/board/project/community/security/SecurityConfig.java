package board.project.community.security;

import board.project.community.repository.UserRepository;
import board.project.community.security.jwt.JwtAuthenticationFilter;
import board.project.community.security.jwt.JwtAuthorizationFilter;
import board.project.community.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserRepository userRepository;
	private final JwtProperties jwtProperties;
	private final CorsFilter corsFilter;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProperties, authenticationManager());
		jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // /login 으로 로그인 프로세스 진행
		return jwtAuthenticationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// No Security
		http
				.addFilter(corsFilter)
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin().disable()
				.httpBasic().disable()
				.addFilter(jwtAuthenticationFilter())
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtProperties))
				.authorizeRequests()
				// login api
				.antMatchers("/login").permitAll()
				.antMatchers("/users/sign-up").permitAll() // sign-up
				.anyRequest().authenticated();
	}
}
