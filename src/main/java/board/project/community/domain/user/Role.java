package board.project.community.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	BLOCKED("ROLE_BLOCKED");

	private final String role;

	@JsonCreator
	public static Role from(String role) {
		return Role.valueOf(role.toUpperCase());
	}
}
