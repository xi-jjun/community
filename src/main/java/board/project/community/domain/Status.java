package board.project.community.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
	ACTIVE("active"),
	BLOCKED("blocked");

	private final String status;
}
