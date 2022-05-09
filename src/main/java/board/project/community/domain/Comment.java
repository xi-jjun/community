package board.project.community.domain;

import board.project.community.controller.dto.request.CommentRequestCreateDTO;
import board.project.community.controller.dto.request.CommentRequestUpdateDTO;
import board.project.community.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Comment {
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@Column
	private int groupIdx;

	@Column
	private int level;

	@Column
	private LocalDateTime createdDate;

	@Column
	private LocalDateTime updatedDate;

	@JsonIgnore
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@ManyToOne(targetEntity = Posting.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "posting_id")
	private Posting posting;

	public Comment(CommentRequestCreateDTO commentRequestCreateDTO) {
		makeCommentInfo(commentRequestCreateDTO);
		this.createdDate = LocalDateTime.now();
	}

	public void initPosting(Posting posting) {
		this.posting = posting;
	}

	public void initUser(User user) {
		this.user = user;
	}

	public void update(CommentRequestUpdateDTO commentRequestUpdateDTO) {
		this.comment = commentRequestUpdateDTO.getComment();
		this.updatedDate = LocalDateTime.now();
	}

	public void removeComment(String comment) {
		this.comment = comment;
	}

	private void makeCommentInfo(CommentRequestCreateDTO commentRequestCreateDTO) {
		this.comment = commentRequestCreateDTO.getComment();
		this.groupIdx = commentRequestCreateDTO.getGroupIdx();
		this.level = commentRequestCreateDTO.getLevel();
	}
}
