package board.project.community.security.jwt;

import board.project.community.domain.user.User;
import board.project.community.security.auth.PrincipalDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Setter
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtProperties jwtProperties;

	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			User user = objectMapper.readValue(request.getInputStream(), User.class);

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword());

			Authentication authentication = authenticationManager.authenticate(authenticationToken);

			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication authResult)
			throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

		String jwtToken = JWT.create()
				.withSubject(jwtProperties.getSubject())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpireTime()))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("account", principalDetails.getUser().getAccount())
				.sign(Algorithm.HMAC512(jwtProperties.getHashKey()));

		response.addHeader("Authorization", jwtProperties.getTOKEN_PREFIX() + jwtToken);
	}
}
