package board.project.community.controller.dto.response;

import board.project.community.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDTO {
	private Long id;
	private int groupIdx;
	private int level;
	private String comment;
	private String commenter;
	private LocalDateTime createdDate;

	public CommentResponseDTO(Comment comment) {
		this.id = comment.getId();
		this.groupIdx = comment.getGroupIdx();
		this.level = comment.getLevel();
		this.comment = comment.getComment();
		this.commenter = comment.getUser().getNickname();
		this.createdDate = getCommentingDate(comment);
	}

	private LocalDateTime getCommentingDate(Comment comment) {
		return comment.getUpdatedDate() == null ? comment.getCreatedDate() : comment.getUpdatedDate();
	}
}
