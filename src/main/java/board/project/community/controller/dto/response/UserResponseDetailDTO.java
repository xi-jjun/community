package board.project.community.controller.dto.response;

import board.project.community.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 로그인을 한 모든 User 권한에 대하여, 사용자의 자세한 정보를 열람하기 위한 정보를 담은 객체
 */
@Data
public class UserResponseDetailDTO {
	private Long id;
	private String nickname;
	private LocalDateTime createdDate;

	public UserResponseDetailDTO(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.createdDate = user.getCreatedDate();
	}
}
