package board.project.community.domain.posting;

import board.project.community.domain.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data // 얘를 안써주면 PostingController GET("/postings/{postingIdx}") 에서 DTO data를 못가지고 온다.
@Builder(builderMethodName = "postingDTOBuilder")
public class PostingDTO {
	private String title;
	private String subtitle;
	private String content;
	private Status status;
	private String writer; // User 에서 가져와야할 정보.
	private String boardName; // Board 에서 가여와야할 정보.
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public static PostingDTOBuilder builder(String title) {
		return postingDTOBuilder();
	}
}
