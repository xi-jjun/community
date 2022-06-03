package board.project.community.controller.dto.response;

import board.project.community.domain.Posting;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostingResponseDTO {
	private String boardName;
	private String title;
	private String subtitle;
	private String content;
	private LocalDateTime createdDate;

	private Long userId;
	private String writer;

//	private Preference preference; // like/dislike

	public PostingResponseDTO(Posting posting) {
		this.boardName = posting.getBoard().getName();
		this.title = posting.getTitle();
		this.subtitle = posting.getSubtitle();
		this.content = posting.getContent();
		this.createdDate = getCreationDate(posting);

		this.userId = posting.getUser().getId();
		this.writer = posting.getUser().getNickname();
	}

	private LocalDateTime getCreationDate(Posting posting) {
		if (posting.getUpdatedDate() == null) {
			return posting.getCreatedDate();
		}

		return posting.getUpdatedDate();
	}
}
