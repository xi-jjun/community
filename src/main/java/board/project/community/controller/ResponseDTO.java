package board.project.community.controller;

import lombok.Data;

@Data
public class ResponseDTO {
	private String message;
	private int statusCode;

	public ResponseDTO(String message, int statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}
}
