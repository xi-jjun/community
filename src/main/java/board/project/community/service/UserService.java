package board.project.community.service;

import board.project.community.controller.ResponseDTO;
import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.controller.dto.response.PostingResponseListDTO;
import board.project.community.controller.dto.response.UserResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
	public ResponseDTO join(UserRequestDTO requestDTO);

	public ResponseDTO updateUserInfo(UserRequestDTO requestDTO, Long id, Authentication authentication);

	public ResponseDTO removeUser(Long id, Authentication authentication);

	public UserResponseDTO findUserById(Long id);

	public List<PostingResponseListDTO> getUserPostings(Long id);

	public UserResponseDTO findUsersByNickname(String nickname);

	/**
	 * admin 계정이면, repository 의 findAll method,
	 * user 계정이면, repository 의 findActiveUsers method 로 결과룰 보여준다.
	 * @param authentication : admin 인지, user 계정인지 확인하기 위해 controller 로 부터 받아오는 인증객체
	 * @return : user list
	 */
	public List<UserResponseDTO> findAllUsers(Authentication authentication);
}
