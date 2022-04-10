package board.project.community.domain.preference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
	COMMENT("comment"),
	POSTING("posting");

	private final String tableType;
}
