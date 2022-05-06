package board.project.community.controller.dto.request;

import board.project.community.domain.user.User;
import lombok.Data;

/**
 * 회원가입 할 때 사용자가 가입할 정보를 담아서 서버에 전달해주는 객체
 * 사용자 본인의 계정에서 정보를 수정할 때, 수정할 정보를 담아서 서버에 전달해주는 객체
 */
@Data
public class UserRequestDTO {
	private String name;
	private String account;
	private String password;
	private String nickname;
	private int age;

	public User toEntity() {
		return new User(this);
	}
}
