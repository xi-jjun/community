package board.project.community.controller.dto.request;

import board.project.community.domain.Status;
import board.project.community.domain.Posting;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 게시물을 작성할 때 사용자가 보내는 요청 형식
 */
@Builder(builderMethodName = "postingRequestBuilder")
@Data
public class PostingRequestDTO {
	private String title;
	private String subtitle;
	private String content;
	private String boardName; // Controller 에를 get method 로 꺼내서 board 에 저장해야함

	/**
	 * POST 요청을 위해 있다.
	 * @return : postingRepository 에 저장하기 위한 Posting Entity
	 */
	public Posting toEntity() {
		Posting posting = Posting.PostingBuilder()
				.title(this.title)
				.subtitle(this.subtitle)
				.content(this.content)
				.status(Status.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();
		return posting;
	}
}
