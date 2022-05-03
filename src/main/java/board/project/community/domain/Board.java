package board.project.community.domain.board;

import board.project.community.domain.Status;
import board.project.community.domain.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Setter // temp
@Getter
@NoArgsConstructor
@Entity
public class Board {
	@Id
	@Column(name = "board_name")
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(mappedBy = "board")
	private List<Posting> postings = new ArrayList<>();

	public Board(String name, Status status) {
		this.name = name;
		this.status = status;
	}

	/**
	 * update logic : name, status
	 * name : 게시판 이름 수정
	 * status : 게시판 차단/활성화 여부 수정
	 * @param name
	 */
	public void updateName(String name) {
		this.name = name;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}
}
