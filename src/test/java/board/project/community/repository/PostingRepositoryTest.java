package board.project.community.repository;

import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.domain.Status;
import board.project.community.domain.board.Board;
import board.project.community.domain.posting.Posting;
import board.project.community.domain.posting.PostingDTO;
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
		Board board = new Board("자유게시판", Status.ACTIVE);
		boardRepository.addBoard(board);

		User user = User.UserBuilder()
				.account("kimJJ")
				.name("김재준")
				.nickname("공돌이는 공돌공돌해")
				.password("pw1234")
				.age(26)
				.role(Role.USER)
				.createdDate(LocalDateTime.now())
				.build();
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
		Board board = new Board("자유게시판", Status.ACTIVE);
		boardRepository.addBoard(board);

		final String nickname = "공돌이는 공돌공돌해";
		User user = User.UserBuilder()
				.account("kimJJ")
				.name("김재준")
				.nickname(nickname)
				.password("pw1234")
				.age(26)
				.role(Role.USER)
				.createdDate(LocalDateTime.now())
				.build();
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
		final String changedBoardName = "정보게시판";
		PostingRequestDTO postingRequestDTO = PostingRequestDTO.postingRequestBuilder()
				.boardName(changedBoardName)
				.title("수정할 제목")
				.subtitle("수정한 부제목")
				.content("내용도 수정했따. 글을 수정했다.")
				.build();

		Posting toBeUpdatePosting = postingRepository.findById(toBeUpdatePostingId);
		toBeUpdatePosting.update(postingRequestDTO);

		em.flush();
		em.clear();

		System.out.println("====================");

		// then
		Posting updatedPosting = postingRepository.findById(postingId);
		assertEquals(toBeUpdatePosting.getId(), updatedPosting.getId());
		assertEquals(toBeUpdatePosting.getTitle(), updatedPosting.getTitle());
		assertEquals(toBeUpdatePosting.getBoard().getName(), updatedPosting.getBoard().getName());
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
		Board board = new Board("자유게시판", Status.ACTIVE);
		boardRepository.addBoard(board);

		final String nickname = "공돌이는 공돌공돌해";
		User user = User.UserBuilder()
				.account("kimJJ")
				.name("김재준")
				.nickname(nickname)
				.password("pw1234")
				.age(26)
				.role(Role.USER)
				.createdDate(LocalDateTime.now())
				.build();
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