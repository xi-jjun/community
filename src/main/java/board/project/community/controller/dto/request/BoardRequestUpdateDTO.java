package board.project.community.controller.dto.request;

import board.project.community.domain.Board;
import board.project.community.domain.Status;
import lombok.Data;

@Data
public class BoardRequestUpdateDTO {
	private String boardName;
	private Status status;
}
