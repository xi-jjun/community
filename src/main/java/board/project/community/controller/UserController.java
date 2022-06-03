package board.project.community.controller;

import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.controller.dto.response.PostingResponseListDTO;
import board.project.community.controller.dto.response.UserResponseDTO;
import board.project.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
	private final UserService userService;

	/**
	 * 관리자만 접근 가능한 모든 사용자 목록 출력.
	 * 향후 인증 관련 코드 넣어야 함.
	 *
	 * @return - 전체 사용자 목록 조회
	 */
	@GetMapping("")
	public List<UserResponseDTO> users(Authentication authentication) {
		List<UserResponseDTO> response = userService.findAllUsers(authentication);
		return response;
	}

	/**
	 * 사용자 상세 정보 조회
	 * @param userId - 조회하려는 사용자의 PK
	 * @return - 사용자 상세 정보
	 */
	@GetMapping("/{userId}")
	public UserResponseDTO user(@PathVariable("userId") String userId) {
		Long id = Long.parseLong(userId);
		UserResponseDTO response = userService.findUserById(id);
		return response;
	}

	/**
	 * 사용자가 적은 모든 게시물 목록 출력
	 * @param userId - 게시물을 적은 사용자 PK
	 * @return - userId 의 사용자가 적은 게시물 목록 리스트 반환
	 */
	@GetMapping("/{userId}/postings")
	public List<PostingResponseListDTO> userPostings(@PathVariable("userId") String userId) {
		Long id = Long.parseLong(userId);
		List<PostingResponseListDTO> response = userService.getUserPostings(id);
		return response;
	}

	/**
	 * 회원 가입
	 * @param userRequestDTO - 회원가입할 정보를 담은 DTO class
	 * @return - 응답 객체 반환
	 */
	@PostMapping("/sign-up")
	public ResponseDTO signUp(@RequestBody UserRequestDTO userRequestDTO) {
		ResponseDTO response = userService.join(userRequestDTO);
		return response;
	}

	/**
	 * 회원 정보 수정
	 * @param userRequestDTO - 수정할 회원 정보가 담긴 DTO class
	 * @param userId - 수정할 회원의 PK
	 * @return - 응답 객체 반환
	 */
	@PatchMapping("/{userId}")
	public ResponseDTO updateUserInfo(@RequestBody UserRequestDTO userRequestDTO,
									  @PathVariable String userId,
									  Authentication authentication) {
		Long id = Long.parseLong(userId);
		ResponseDTO response = userService.updateUserInfo(userRequestDTO, id, authentication);
		return response;
	}
}
