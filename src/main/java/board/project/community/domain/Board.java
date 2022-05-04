package board.project.community.domain;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@Column
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(mappedBy = "board")
	private List<Posting> postings = new ArrayList<>();

	/**
	 * Board Entity 생성을 위한 생성자.
	 * @param boardRequestCreateDTO : client 에게로 부터 board 를 생성하기 위해 받은 데이터
	 */
	public Board(BoardRequestCreateDTO boardRequestCreateDTO) {
		this.name = boardRequestCreateDTO.getBoardName();
		this.status = Status.ACTIVE;
	}

	/**
	 * update logic : name, status
	 * name : 게시판 이름 수정
	 * status : 게시판 차단/활성화 여부 수정
	 * @param name : 수정될 이름
	 */
	public void updateName(String name) {
		this.name = name;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}
}
