package board.project.community.repository;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.Status;
import board.project.community.domain.Board;
import board.project.community.domain.Posting;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional(readOnly = true)
@SpringBootTest
class PostingRepositoryTest {
	@Autowired
	private PostingRepository postingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private EntityManager em;

	@Transactional
	@Test
	void save() {
		// given
		BoardRequestCreateDTO dto = new BoardRequestCreateDTO();
		dto.setBoardName("자유게시판");
		Board board = dto.toEntity();
		boardRepository.addBoard(board);


		UserRequestDTO userDto = new UserRequestDTO();
		userDto.setAccount("kimJJ");
		userDto.setPassword("pw1234");
		userDto.setName("김재준");
		userDto.setNickname("공돌이는 공돌공돌해");
		userDto.setAge(26);
		User user = userDto.toEntity();

		userRepository.save(user);


		// when
		Posting posting = Posting.PostingBuilder()
				.title("My Title")
				.subtitle("this is subtitle")
				.content("여기는 글의 내용을 쓰는 곳입니다.")
				.status(Status.ACTIVE)
				.createdDate(LocalDateTime.now())
				.user(user)
				.board(board)
				.build();
		postingRepository.save(posting);

		em.flush();
		em.clear();

		Posting findPosting = postingRepository.findById(posting.getId());

		// then
		assertEquals(posting.getId(), findPosting.getId());
		assertEquals(posting.getTitle(), findPosting.getTitle());
		assertEquals(posting.getSubtitle(), findPosting.getSubtitle());
		assertEquals(posting.getContent(), findPosting.getContent());
		assertEquals(posting.getStatus(), findPosting.getStatus());
		assertEquals(posting.getCreatedDate(), findPosting.getCreatedDate());
		assertEquals(posting.getBoard().getName(), findPosting.getBoard().getName());
		assertEquals(posting.getUser().getNickname(), findPosting.getUser().getNickname());
	}

	@Transactional
	@Test
	void update() {
		// given
		BoardRequestCreateDTO dto = new BoardRequestCreateDTO();
		dto.setBoardName("자유게시판");
		Board board = dto.toEntity();
		boardRepository.addBoard(board);

		BoardRequestCreateDTO dto2 = new BoardRequestCreateDTO();
		dto2.setBoardName("정보게시판");
		Board board2 = dto2.toEntity();
		boardRepository.addBoard(board2);


		final String nickname = "공돌이는 공돌공돌해";
		UserRequestDTO userDto = new UserRequestDTO();
		userDto.setAccount("kimJJ");
		userDto.setPassword("pw1234");
		userDto.setName("김재준");
		userDto.setNickname(nickname);
		userDto.setAge(26);
		User user = userDto.toEntity();
		userRepository.save(user);


		Posting posting = Posting.PostingBuilder()
				.title("My Title")
				.subtitle("this is subtitle")
				.content("여기는 글의 내용을 쓰는 곳입니다.")
				.status(Status.ACTIVE)
				.createdDate(LocalDateTime.now())
				.user(user)
				.board(board)
				.build();
		postingRepository.save(posting);

		Long postingId = posting.getId();

		em.flush();
		em.clear();


		// when
		final Long toBeUpdatePostingId = postingId;

		// update data
		final String CHANGED_BOARD_NAME = "정보게시판";
		PostingRequestDTO postingRequestDTO = PostingRequestDTO.postingRequestBuilder()
				.boardName(CHANGED_BOARD_NAME)
				.title("수정할 제목")
				.subtitle("수정한 부제목")
				.content("내용도 수정했따. 글을 수정했다.")
				.build();

		Posting toBeUpdatePosting = postingRepository.findById(toBeUpdatePostingId);
		Board changedBoard = boardRepository.findByName(CHANGED_BOARD_NAME);
		toBeUpdatePosting.updateBoard(changedBoard); // Board 를 바꿔준다.
		toBeUpdatePosting.update(postingRequestDTO);

		em.flush();
		em.clear();

		System.out.println("====================");

		// then
		Posting updatedPosting = postingRepository.findById(postingId);
		assertEquals(toBeUpdatePosting.getId(), updatedPosting.getId());
		assertEquals(toBeUpdatePosting.getTitle(), updatedPosting.getTitle());
		assertEquals(CHANGED_BOARD_NAME, updatedPosting.getBoard().getName());
		assertEquals(toBeUpdatePosting.getSubtitle(), updatedPosting.getSubtitle());
		assertEquals(toBeUpdatePosting.getContent(), updatedPosting.getContent());
		assertEquals(nickname, updatedPosting.getUser().getNickname());
		assertEquals(toBeUpdatePosting.getStatus(), updatedPosting.getStatus());
		assertNotNull(updatedPosting.getUpdatedDate());
		assertEquals(toBeUpdatePosting.getUpdatedDate(), updatedPosting.getUpdatedDate());
	}

	@Transactional
	@Test
	void remove() {
		// given
		BoardRequestCreateDTO dto = new BoardRequestCreateDTO();
		dto.setBoardName("자유게시판");
		Board board = dto.toEntity();
		boardRepository.addBoard(board);


		final String nickname = "공돌이는 공돌공돌해";
		UserRequestDTO userDto = new UserRequestDTO();
		userDto.setAccount("kimJJ");
		userDto.setPassword("pw1234");
		userDto.setName("김재준");
		userDto.setNickname(nickname);
		userDto.setAge(26);
		User user = userDto.toEntity();

		userRepository.save(user);


		Posting posting = Posting.PostingBuilder()
				.title("My Title")
				.subtitle("this is subtitle")
				.content("여기는 글의 내용을 쓰는 곳입니다.")
				.status(Status.ACTIVE)
				.createdDate(LocalDateTime.now())
				.user(user)
				.board(board)
				.build();
		postingRepository.save(posting);

		Long postingId = posting.getId();

		em.flush();
		em.clear();


		// when : remove
		postingRepository.remove(postingId);

		em.flush();
		em.clear();


		// then
		Posting removedPosting = postingRepository.findById(postingId);

		assertNull(removedPosting);
	}
}