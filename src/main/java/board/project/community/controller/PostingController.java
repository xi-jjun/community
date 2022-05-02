package board.project.community.controller;

import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.controller.dto.response.PostingResponseDetailDTO;
import board.project.community.controller.dto.response.PostingResponseListDTO;
import board.project.community.domain.board.Board;
import board.project.community.domain.posting.Posting;
import board.project.community.domain.posting.PostingDTO;
import board.project.community.domain.user.User;
import board.project.community.repository.BoardRepository;
import board.project.community.repository.PostingRepository;
import board.project.community.repository.UserRepository;
import board.project.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequestMapping("/community/postings")
@RequiredArgsConstructor
@RestController
public class PostingController {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final PostingRepository postingRepository;

	@GetMapping("")
	public List<PostingResponseListDTO> findPostings() {
		List<Posting> postings = postingRepository.findAll();
		return postings.stream()
				.map(PostingResponseListDTO::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/{postingId}")
	public PostingResponseDetailDTO findPosting(@PathVariable("postingId") String postingId) {
		Long id = Long.parseLong(postingId);
		Posting findPosting = postingRepository.findById(id);
		return new PostingResponseDetailDTO(findPosting);
	}

	@PostMapping("")
	public ResponseDTO posting(@RequestBody PostingRequestDTO postingRequestDTO) {
		// user 얻고,
		String boardName = postingRequestDTO.getBoardName();
		Board board = boardRepository.findByName(boardName);

		Posting posting = postingRequestDTO.toEntity();
		posting.setBoard(board);

		// session, security 써서 거기서 찾아서 넣어줘야 함.
		posting.setUser(null);

		postingRepository.save(posting);

		return new ResponseDTO("posting is ok", 200);
	}

	@Transactional
	@PatchMapping("/{postingId}")
	public ResponseDTO updatePosting(@RequestBody PostingRequestDTO postingRequestDTO,
									 @PathVariable String postingId) {
		Long id = Long.parseLong(postingId);
		Posting findPosting = postingRepository.findById(id);
		findPosting.update(postingRequestDTO);

		/**
		 * 없는 board name 에 대해서 예외처리 필요함. 향후 구현
		 * 게시물 작성자는 안바뀌므로 신경 x
		 */
		String substitutionBoardName = postingRequestDTO.getBoardName();
		Board substitutionBoard = boardRepository.findByName(substitutionBoardName);
		findPosting.setBoard(substitutionBoard);

		return new ResponseDTO("good", 200);
	}

	/**
	 * userController 에 있는게 맞는 것 같다
	 */
//	@GetMapping("/nickname/{userNickname}")
//	public List<PostingSearchDTO> findAllUserPostings(@PathVariable("userNickname") String userNickname) {
//		List<Posting> postings = postingService.findByNickname(userNickname);
//		return postings.stream()
//				.map(PostingSearchDTO::new)
//				.collect(Collectors.toList());
//	}
}
