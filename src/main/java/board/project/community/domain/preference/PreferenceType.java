package board.project.community.domain.preference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PreferenceType {
	LIKE("like"),
	DISLIKE("dislike"),
	NONE("none");

	private final String status;
}
