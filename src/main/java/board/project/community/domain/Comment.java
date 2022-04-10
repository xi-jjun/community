package board.project.community.domain;

import board.project.community.domain.posting.Posting;
import board.project.community.domain.user.User;
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
	@Column(name = "comment_idx")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column
	private String comment;

	@Column
	private int groupIdx;

	@Column
	private int level;

	@Column
	private LocalDateTime createdDate;

	@Column
	private LocalDateTime updatedDate;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private User user;

	@ManyToOne(targetEntity = Posting.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "posting_idx")
	private Posting posting;
}
