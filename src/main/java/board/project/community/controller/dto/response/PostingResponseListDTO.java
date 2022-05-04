package board.project.community.controller.dto.response;

import board.project.community.domain.Posting;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostingResponseListDTO {
	private Long id;
	private String title;
	private String writer;
	private LocalDateTime createdDate;
	private String boardName;

	public PostingResponseListDTO(Posting posting) {
		this.id = posting.getId();
		this.title = posting.getTitle();
		this.writer = posting.getUser().getNickname();
		this.createdDate = getCreationDate(posting);
		this.boardName = posting.getBoard().getName();
	}

	private LocalDateTime getCreationDate(Posting posting) {
		if (posting.getUpdatedDate() == null) {
			return posting.getCreatedDate();
		}

		return posting.getUpdatedDate();
	}
}
