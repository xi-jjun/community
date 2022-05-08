package board.project.community.domain;

import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.domain.preference.Preference;
import board.project.community.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor // Builder 와 NoArgsConstructor 는 같이 쓰이지 못하기 때문에 써줘야한다.
@Entity
public class Posting {
	@Id
	@Column(name = "posting_id") // DB column name
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	private LocalDateTime createdDate = null;

	@Column
	private LocalDateTime updatedDate = null;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE)
	private List<Preference> preferences = new ArrayList<>();

	@OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();

	public Posting(PostingRequestDTO postingRequestDTO) {
		this.title = postingRequestDTO.getTitle();
		this.subtitle = postingRequestDTO.getSubtitle();
		this.content = postingRequestDTO.getContent();
		this.status = Status.ACTIVE;
		this.createdDate = LocalDateTime.now();
	}

	public void update(PostingRequestDTO postingRequestDTO) {
		this.title = postingRequestDTO.getTitle();
		this.subtitle = postingRequestDTO.getSubtitle();
		this.content = postingRequestDTO.getContent();
		this.updatedDate = LocalDateTime.now();
	}

	/**
	 * 관리자 + 게시물 작성자에게만 허락되는 기능.
	 * @param status : 게시물 상태. ACTIVE, BLOCKED
	 */
	public void updateStatus(Status status) {
		this.status = status;
	}

	public void updateBoard(Board board) {
		this.board = board;
	}

	/**
	 * 게시물에 대해 누가 작성했는지에 관한 method
	 * @param user : this 게시물 작성한 User 객체
	 */
	public void initUser(User user) {
		this.user = user;
	}
}
