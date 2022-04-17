package board.project.community.domain.posting;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostingSearchDTO {
	private final Long postingIdx;
	private final String title;
	private final LocalDateTime createDate;
	private final String writer;

	public PostingSearchDTO(Posting posting) {
		this.postingIdx = posting.getIdx();
		this.title = posting.getTitle();
		this.createDate = posting.getUpdatedDate() == null ? posting.getCreatedDate() : posting.getCreatedDate();
		this.writer = "Temp User";
//		this.writer = posting.getUser().getNickname();
	}
}
