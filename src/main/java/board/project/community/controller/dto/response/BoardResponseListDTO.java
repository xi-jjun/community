package board.project.community.controller.dto.response;

import board.project.community.domain.Board;
import lombok.Data;

@Data
public class BoardResponseListDTO {
	private String boardName;

	public BoardResponseListDTO(Board board) {
		this.boardName = board.getName();
	}
}
