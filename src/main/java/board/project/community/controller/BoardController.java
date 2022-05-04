package board.project.community.controller;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import board.project.community.controller.dto.request.BoardRequestUpdateDTO;
import board.project.community.controller.dto.response.PostingResponseListDTO;
import board.project.community.domain.Board;
import board.project.community.domain.Posting;
import board.project.community.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequiredArgsConstructor
@RequestMapping("/community") // root path 명확히 해야한다
@RestController
public class BoardController {
	private final BoardRepository boardRepository;

	@GetMapping("/{boardName}")
	public List<PostingResponseListDTO> boardMain(@PathVariable("boardName") String boardName) {
		Board board = boardRepository.findByName(boardName);
		List<Posting> postings = board.getPostings();
		return postings.stream()
				.map(PostingResponseListDTO::new)
				.collect(Collectors.toList());
	}

	/**
	 * ADMIN 권한을 가진 사용자만이 게시판을 생성할 수 있다는 요구사항이 있음.
	 * @param boardRequestCreateDTO : 게시판 생성 데이터
	 * @return : 200 is ok
	 */
	@Transactional
	@PostMapping("")
	public ResponseDTO makeBoard(@RequestBody BoardRequestCreateDTO boardRequestCreateDTO) {
		Board board = boardRequestCreateDTO.toEntity();
		boardRepository.addBoard(board);

		return new ResponseDTO("success to make new board!", 200);
	}

	/**
	 * ADMIN 권한을 가진 사용자만이 게시판의 정보를 수정할 수 있다는 요구사항이 있음.
	 * @param boardRequestUpdateDTO : 수정할 정보가 담긴 객체
	 * @param boardName : 수정될 게시판의 이름
	 * @return : 200 is ok
	 */
	@Transactional
	@PatchMapping("{boardName}")
	public ResponseDTO updateBoard(@RequestBody BoardRequestUpdateDTO boardRequestUpdateDTO,
								   @PathVariable String boardName) {
		Board toBeUpdateBoard = boardRepository.findByName(boardName);
		toBeUpdateBoard.updateName(boardRequestUpdateDTO.getBoardName());
		toBeUpdateBoard.updateStatus(boardRequestUpdateDTO.getStatus());

		return new ResponseDTO("success to update the board", 200);
	}
}
