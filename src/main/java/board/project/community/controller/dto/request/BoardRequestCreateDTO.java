package board.project.community.controller.dto.request;

import board.project.community.domain.Board;
import lombok.Data;

@Data
public class BoardRequestCreateDTO {
	private String boardName;

	public Board toEntity() {
		Board board = new Board(this);
		return board;
	}
}
