package board.project.community.domain.posting;

import board.project.community.domain.Board;
import board.project.community.domain.Status;
import board.project.community.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Setter // temp
@Getter
@Entity
public class Posting {
	@Id
	@Column(name = "posting_idx") // DB column name
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column
	private String title;

	@Column
	private String subtitle;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column
	private LocalDateTime createdDate;

	@Column
	private LocalDateTime updatedDate;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private User user;

	@ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "board_idx")
	private Board board;
}
