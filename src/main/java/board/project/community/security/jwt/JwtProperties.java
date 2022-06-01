package board.project.community.security.jwt;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {
	private final String subject = "atk";
	private final int expireTime = 60000;
	private final String hashKey = "atk_secret";

	private final String TOKEN_PREFIX = "Bearer ";
}
