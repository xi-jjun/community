package board.project.community.controller;

import board.project.community.controller.dto.request.CommentRequestCreateDTO;
import board.project.community.controller.dto.request.CommentRequestUpdateDTO;
import board.project.community.controller.dto.response.CommentResponseDTO;
import board.project.community.domain.Comment;
import board.project.community.domain.Posting;
import board.project.community.repository.CommentRepository;
import board.project.community.repository.PostingRepository;
import board.project.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
	private final int TOP_LEVEL = 0;
	private final String REMOVE_MESSAGE = "(삭제된 댓글입니다)";
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final PostingRepository postingRepository;

	/**
	 * test 전용. 안쓰일 가능성이 높다.
	 * @return : 현재 커뮤니티에 존재하는 모든 댓글 가져오기
	 */
	@GetMapping("")
	public List<CommentResponseDTO> findAllComments() {
		List<Comment> comments = commentRepository.findAll();
		return comments.stream()
				.map(CommentResponseDTO::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/{commentId}")
	public CommentResponseDTO findComment(@PathVariable String commentId) {
		Long id = Long.parseLong(commentId);
		Comment comment = commentRepository.findById(id);
		return new CommentResponseDTO(comment);
	}

	/**
	 * url path 에 postings 를 앞에 적어준 이유 : {postingId} 라는 자원을 그저 '/comments/{postingId}'로 하면
	 * 명확하지가 않다고 판단했기 때문.
	 * @param createDTO
	 * @param postingId
	 * @return
	 */
	@Transactional
	@PostMapping("/postings/{postingId}")
	public ResponseDTO comment(@RequestBody CommentRequestCreateDTO createDTO,
							   @PathVariable String postingId) {
		Comment comment = createDTO.toEntity();
		Posting commentedPosting = getPosting(postingId);
		comment.initPosting(commentedPosting);
		comment.initUser(null); // 댓글을 쓰는 사용자 정보 찾아서 넣어줘야 한다.

		commentRepository.save(comment);

		return new ResponseDTO("success to comment!", 200);
	}

	/**
	 * 나중에 service layer 만들어서 다 옮겨야 한다.
	 * @param postingId : 댓글이 달릴 게시물 PK
	 * @return : 해당 게시물 entity
	 */
	private Posting getPosting(String postingId) {
		Long id = Long.parseLong(postingId);
		Posting posting = postingRepository.findById(id);
		return posting;
	}

	@Transactional
	@PatchMapping("/{commentId}")
	public ResponseDTO update(@RequestBody CommentRequestUpdateDTO updateDTO,
							  @PathVariable String commentId) {
		Long id = Long.parseLong(commentId);
		Comment updateComment = commentRepository.findById(id);
		updateComment.update(updateDTO);

		return new ResponseDTO("success to update comment", 200);
	}

	/**
	 * 삭제할 때에는 해당 댓글을 없애는 것이 아닌, '삭제됐습니다' 로 표시. => 에브리타임 참고
	 *
	 * @param commentId : 현재 삭제할 댓글 PK
	 * @return : 응답객체
	 */
	@Transactional
	@DeleteMapping("/{commentId}")
	public ResponseDTO remove(@PathVariable String commentId) {
		Long id = Long.parseLong(commentId);
		Comment comment = commentRepository.findById(id);
		comment.removeComment(REMOVE_MESSAGE);

		return new ResponseDTO("success to remove comment!", 200);
	}
}
