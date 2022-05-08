package board.project.community.controller.dto.request;

import board.project.community.domain.Comment;
import lombok.Data;

/**
 * 사용자가 댓글을 작성하려고 할 때 작성한 정보들을 담아서 서버에 전달해주는 DTO class
 */
@Data
public class CommentRequestCreateDTO {
	private String comment;
	private int groupIdx;
	private int level;

	public Comment toEntity() {
		return new Comment(this);
	}
}
