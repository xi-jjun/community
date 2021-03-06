package board.project.community.service.impl;

import board.project.community.controller.ResponseDTO;
import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.controller.dto.response.PostingResponseListDTO;
import board.project.community.controller.dto.response.UserResponseDTO;
import board.project.community.domain.Posting;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import board.project.community.repository.UserRepository;
import board.project.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public ResponseDTO join(UserRequestDTO requestDTO) {
		if (requestDTO == null) {
			return new ResponseDTO("Invalid client request", 400);
		}

		if (validateDuplicated(requestDTO)) {
			String password = requestDTO.getPassword();
			String encodedPassword = bCryptPasswordEncoder.encode(password);
			requestDTO.setPassword(encodedPassword);

			User user = requestDTO.toEntity();
			userRepository.save(user);

			return new ResponseDTO("success to sign up user", 200);
		}

		return new ResponseDTO("fail to sign up user", 400);
	}

	private boolean validateDuplicated(UserRequestDTO requestDTO) {
		String requestAccount = requestDTO.getAccount();
		String requestNickname = requestDTO.getNickname();
		User duplicatedAccount = userRepository.findByAccount(requestAccount);
		User duplicatedNickname = userRepository.findByNickname(requestNickname);

		return duplicatedAccount == null && duplicatedNickname == null;
	}

	@Transactional
	@Override
	public ResponseDTO updateUserInfo(UserRequestDTO requestDTO, Long id, Authentication authentication) {
		if (requestDTO == null || id == null) {
			return new ResponseDTO("Invalid client request", 400);
		} else if (authentication == null || !validateAuthentication(authentication, id)) {
			return new ResponseDTO("has no authentication to update", 400);
		} else if (validateDuplicateUpdateRequest(requestDTO, id)) {
			return new ResponseDTO("Duplicated account, nickname", 400);
		}

		userRepository.update(requestDTO, id);

		return new ResponseDTO("success to update user", 200);
	}

	private boolean validateDuplicateUpdateRequest(UserRequestDTO requestDTO, Long requestId) {
		User loginUser = userRepository.findById(requestId);

		// client ??? ?????? ????????? ????????? account, nickname ??????
		String updatedAccount = requestDTO.getAccount();
		String updatedNickname = requestDTO.getNickname();

		// ????????? ?????? ??? ?????? ?????? ???????????? ??????
		User findByAccount = userRepository.findByAccount(updatedAccount);
		String alreadyExistedAccount = findByAccount.getAccount();
		User findByNickname = userRepository.findByNickname(updatedNickname);
		String alreadyExistedNickname = findByNickname.getNickname();

		// ?????? ???????????? ???????????? ????????? ????????? ????????? ???????????? user ??? ??????????????? return false. invalid
		if (!findByAccount.getId().equals(requestId) && alreadyExistedAccount.equals(updatedAccount)
				|| !findByNickname.getId().equals(requestId) && alreadyExistedNickname.equals(updatedNickname)) {
			return false;
		}

		return true;
	}

	private boolean validateAuthentication(Authentication authentication, Long requestId) {
		String loginUserAccount = authentication.getName();
		if (loginUserAccount == null) {
			return false;
		}

		User loginUserEntity = userRepository.findByAccount(loginUserAccount);

		if (!loginUserEntity.getId().equals(requestId)) {
			return false;
		}

		return true;
	}

	/**
	 * admin ??? ?????? ????????? ?????? ??? ??????.
	 * @param id : ???????????? user id
	 * @param authentication : ???????????? ????????? ????????????
	 * @return : ResponseDTO
	 */
	@Transactional
	@Override
	public ResponseDTO removeUser(Long id, Authentication authentication) {
		if (id == null || authentication == null) {
			return new ResponseDTO("Invalid client request", 400);
		}

		String loginUserAccount = authentication.getName();
		User loginUser = userRepository.findByAccount(loginUserAccount);
		Role role = loginUser.getRole();
		if (!loginUser.getId().equals(id) && role.equals(Role.USER)) {
			return new ResponseDTO("has no authentication to remove", 400);
		}

		userRepository.remove(loginUser);

		return new ResponseDTO("success to remove user", 200);
	}

	@Override
	public UserResponseDTO findUserById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Invalid client request");
		}

		User findUser = userRepository.findById(id);

		return new UserResponseDTO(findUser);
	}

	@Override
	public List<PostingResponseListDTO> getUserPostings(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Invalid client request");
		}

		User user = userRepository.findById(id);
		List<Posting> postings = user.getPostings();

		return postings.stream()
				.map(PostingResponseListDTO::new)
				.collect(Collectors.toList());
	}

	@Override
	public UserResponseDTO findUsersByNickname(String nickname) {
		if (nickname == null) {
			throw new IllegalArgumentException("Invalid client request");
		}

		User findUser = userRepository.findByNickname(nickname);

		return new UserResponseDTO(findUser);
	}

	/**
	 * admin ??? ????????? ?????? : Role.BLOCKED ????????? ???????????? ??????
	 * user ??? ????????? ?????? : Role.BLOCKED ??? ?????? ????????? ?????? ??????
	 * @param authentication : admin ??????, user ???????????? ???????????? ?????? controller ??? ?????? ???????????? ????????????
	 * @return : user list
	 */
	@Override
	public List<UserResponseDTO> findAllUsers(Authentication authentication) {
		if (authentication == null) {
			throw new IllegalArgumentException("Invalid client request");
		}

		String loginUserAccount = authentication.getName();
		User loginUser = userRepository.findByAccount(loginUserAccount);
		Role role = loginUser.getRole();

		if (role.equals(Role.USER)) {
			List<User> users = userRepository.findActiveUsers();
			return getUserResponseDTOList(users);
		} else if (role.equals(Role.ADMIN)) {
			List<User> users = userRepository.findAll();
			return getUserResponseDTOList(users);
		}

		throw new IllegalArgumentException("has no authentication");
	}

	private List<UserResponseDTO> getUserResponseDTOList(List<User> users) {
		return users.stream()
				.map(UserResponseDTO::new)
				.collect(Collectors.toList());
	}
}
