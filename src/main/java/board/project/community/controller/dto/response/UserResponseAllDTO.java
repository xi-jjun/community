package board.project.community.controller.dto.response;

import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 관리자가 사용자들을 관리하기 위해서, 사용자 목록을 열람할 때 전달하기 위한 정보들.
 */
@Data
public class UserResponseAllDTO {
	private Long id;
	private String name;
	private String nickname;
	private String account;
	private LocalDateTime createdDate;
	private int age;
	private Role role;

	public UserResponseAllDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.account = user.getAccount();
		this.createdDate = user.getCreatedDate();
		this.age = user.getAge();
		this.role = user.getRole();
	}
}
