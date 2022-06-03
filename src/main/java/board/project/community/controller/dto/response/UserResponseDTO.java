package board.project.community.controller.dto.response;

import board.project.community.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 로그인을 한 모든 User 권한에 대하여, 사용자의 자세한 정보를 열람하기 위한 정보를 담은 객체
 */
@Data
public class UserResponseDTO {
	private Long id;
	private String account;
	private String name;
	private String nickname;
	private String role;
	private int age;
	private LocalDateTime createdDate;

	public UserResponseDTO(User user) {
		this.id = user.getId();
		this.account = user.getAccount();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.role = user.getRole().name();
		this.age = user.getAge();
		this.createdDate = user.getCreatedDate();
	}
}
