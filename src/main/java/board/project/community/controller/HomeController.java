package board.project.community.controller;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import board.project.community.controller.dto.request.CommentRequestCreateDTO;
import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.controller.dto.response.BoardResponseListDTO;
import board.project.community.domain.Comment;
import board.project.community.domain.Status;
import board.project.community.domain.Board;
import board.project.community.domain.Posting;
import board.project.community.domain.user.Role;

import board.project.community.domain.user.User;
import board.project.community.repository.BoardRepository;
import board.project.community.repository.CommentRepository;
import board.project.community.repository.UserRepository;
import board.project.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HomeController {
	private final BoardRepository boardRepository;
	private final PostingService postingService;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	@GetMapping("/")
	public String home() {
		make();
		return "home";
	}

	@GetMapping("/community")
	public List<BoardResponseListDTO> boards() {
		List<Board> boards = boardRepository.findAll();
		for (Board board : boards) {
			log.info("{}", board.getName());
		}
		return boards.stream()
				.map(BoardResponseListDTO::new)
				.collect(Collectors.toList());
	}

	/**
	 * make the dummy data for testing
	 */
	public void make() {
		String[] names = {"Kjj", "user2", "udofsd", "illiillil"};
		User[] users = new User[4];

		for (int i = 0; i < names.length; i++) {
			UserRequestDTO dto = new UserRequestDTO();
			dto.setAccount("user" + i);
			dto.setPassword("123p" + i);
			dto.setName("user" + i);
			dto.setNickname("공공+i" + i);
			dto.setAge(13 + i);

			User user = dto.toEntity();
			userRepository.save(user);
			users[i] = user;
		}

		BoardRequestCreateDTO dto1 = new BoardRequestCreateDTO();
		dto1.setBoardName("자유게시판");
		Board board1 = dto1.toEntity();
		boardRepository.addBoard(board1);

		BoardRequestCreateDTO dto2 = new BoardRequestCreateDTO();
		dto2.setBoardName("정보게시판");
		Board board2 = dto2.toEntity();
		boardRepository.addBoard(board2);

		Board[] boards = {board1, board2};
		int[] commentLevel = {0, 0, 0, 1, 0, 1};
		int[] commentGroupIdx = {1, 2, 3, 2, 4, 1};

		for (int i = 0; i < 11; i++) {
			PostingRequestDTO postingDTO = new PostingRequestDTO();
			postingDTO.setTitle("title " + i);
			postingDTO.setSubtitle("subtitle " + i);
			postingDTO.setContent("content content content " + i);
			postingDTO.setBoardName(boards[i % 2].getName());

			Posting posting = postingDTO.toEntity();
			posting.initUser(users[i % 4]);
			posting.updateBoard(boards[i % 2]);

			postingService.posting(posting);

//			 문제 발생.
			for (int j = 0; j < 3; j++) {
				CommentRequestCreateDTO cmtDTO = new CommentRequestCreateDTO();
				cmtDTO.setComment("게시물 " + (i + 1) + "번,  댓글 " + (j + 1) + "번");
				cmtDTO.setLevel(commentLevel[j]);
				cmtDTO.setGroupIdx(commentGroupIdx[j]);

				Comment comment = cmtDTO.toEntity();
				comment.initPosting(posting);
				comment.initUser(users[j % 4]);
				commentRepository.save(comment);
			}
		}

//		Posting posting = postingService.findByIdx(1L);
//
//		for (int i = 0; i < 6; i++) {
//			CommentRequestCreateDTO cmtDTO = new CommentRequestCreateDTO();
//			cmtDTO.setComment("게시물의 순서 " + (i + 1));
//			cmtDTO.setLevel(commentLevel[i]);
//			cmtDTO.setGroupIdx(commentGroupIdx[i]);
//
//			Comment comment = cmtDTO.toEntity();
//			comment.initPosting(posting);
//			comment.initUser(users[i % 4]);
//			commentRepository.save(comment);
//		}
	}
}
