package board.project.community.controller.dto.request;

import board.project.community.domain.user.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 회원가입 할 때 사용자가 가입할 정보를 담아서 서버에 전달해주는 객체
 * 사용자 본인의 계정에서 정보를 수정할 때, 수정할 정보를 담아서 서버에 전달해주는 객체
 */
@Data
public class UserRequestDTO {
	private String account;
	@NotNull(message = "password 는 null 일 수 없습니다.")
	@Size(min = 5, max = 25, message = "password 는 5~25 자 사이여야 합니다.")
	private String password;
	private String name;
	private String nickname;
	private int age;
	private String role;

	public User toEntity() {
		return new User(this);
	}
}
