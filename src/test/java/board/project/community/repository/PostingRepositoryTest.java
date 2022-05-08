package board.project.community.repository;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import board.project.community.controller.dto.request.CommentRequestCreateDTO;
import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.Comment;
import board.project.community.domain.Status;
import board.project.community.domain.Board;
import board.project.community.domain.Posting;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
	private CommentRepository commentRepository;

	@Autowired
	private EntityManager em;

	@Transactional
	@BeforeAll
	void makeInitData() {
		// 게시판 생성
		BoardRequestCreateDTO boardDTO1 = new BoardRequestCreateDTO();
		boardDTO1.setBoardName("자유게시판");
		Board board1 = boardDTO1.toEntity();

		BoardRequestCreateDTO boardDTO2 = new BoardRequestCreateDTO();
		boardDTO2.setBoardName("정보게시판");
		Board board2 = boardDTO2.toEntity();
		boardRepository.addBoard(board1);
		boardRepository.addBoard(board2);


		// 사용자 계정 생성
		UserRequestDTO userDTO1 = new UserRequestDTO();
		userDTO1.setAccount("account11");
		userDTO1.setPassword("123");
		userDTO1.setName("김재준");
		userDTO1.setNickname("공돌이는공돌공돌해");
		userDTO1.setAge(26);

		User user1 = userDTO1.toEntity();
		userRepository.save(user1);

		UserRequestDTO userDTO2 = new UserRequestDTO();
		userDTO2.setAccount("account__2");
		userDTO2.setPassword("32123");
		userDTO2.setName("김개발");
		userDTO2.setNickname("개발의_신");
		userDTO2.setAge(23);

		User user2 = userDTO2.toEntity();
		userRepository.save(user2);


		// 게시물 생성 : 자유게시판, 정보게시판 각각에 1개씩 생성
		// 사용자로부터 게시물 생성 때 받을 게시물에 대한 정보를 여기 DTO 에 담아서 서버로 가져온다.
		PostingRequestDTO postingsDTO1 = new PostingRequestDTO();
		postingsDTO1.setTitle("게시물 제목 1");
		postingsDTO1.setSubtitle("이거 부제목입니다 12313131");
		postingsDTO1.setContent("내용. 1001010110110101010101 나는 컴퓨터다 101010101010101010101111");
		postingsDTO1.setBoardName("자유게시판");

		Posting posting1 = postingsDTO1.toEntity();
		posting1.initUser(user1); // 사용자1이 게시물을 작성했다는 시나리오
		postingRepository.save(posting1);

		PostingRequestDTO postingsDTO2 = new PostingRequestDTO();
		postingsDTO2.setTitle("게시물 제목 2");
		postingsDTO2.setSubtitle("이거 222222부2제2목2입22니2다2 212313131");
		postingsDTO2.setContent("제곧내(제목이 곧 내용 이라는 뜻ㅎ)");
		postingsDTO2.setBoardName("정보게시판");

		Posting posting2 = postingsDTO2.toEntity();
		posting2.initUser(user2); // 사용자2가 게시물을 작성했다는 시나리오
		postingRepository.save(posting2);


		// 댓글 생성
		CommentRequestCreateDTO commentDTO1 = new CommentRequestCreateDTO();
		commentDTO1.setComment("아 ㅋㅋ 컴퓨터 언어 그거 아닌데 ㅋㅋ");
		// groupIdx 와 level 은 front 에서 처리해서 서버에 전달해주는 방식을 채택할 것이다.
		commentDTO1.setLevel(0); // 대댓글인지 판단해주는 field
		commentDTO1.setGroupIdx(1); // 몇번째 그룹의 대장 댓글인지 알려주는 field

		Comment comment = commentDTO1.toEntity();
		comment.initUser(user1); // 사용자1이..
		comment.initPosting(posting1); // ..게시물1에 댓글을 작성했다고 가정하자.
		commentRepository.save(comment);
	}

	@Transactional
	@Test
	void save() {
		// given : 게시물로 작성할 게시물 정보 + 작성자의 정보가 주어진다.
		final String TITLE = "My Title";
		final String SUBTITLE = "this is subtitle";
		final String CONTENT = "여기는 글의 내용을 쓰는 곳입니다.";
		final String BOARD_NAME = "자유게시판";
		PostingRequestDTO postingRequestDTO = new PostingRequestDTO();
		postingRequestDTO.setTitle(TITLE);
		postingRequestDTO.setSubtitle(SUBTITLE);
		postingRequestDTO.setContent(CONTENT);
		postingRequestDTO.setBoardName(BOARD_NAME);

		User postingUser = userRepository.findById(1L); // 게시물 작성자에 대한 정보. 주어진다.
		final String USER_NICKNAME = postingUser.getNickname();


		// when
		Posting posting = postingRequestDTO.toEntity();
		Board postingBoard = boardRepository.findByName(postingRequestDTO.getBoardName()); // 어느 게시판에 저장할 것인지
		posting.initUser(postingUser); // 게시물 작성자 정보 연관지어 준다.
		posting.updateBoard(postingBoard); // 어떤 게시판에 쓸 것인지 연관지어 준다.

		postingRepository.save(posting); // postingUser 가 작성한 게시물을 저장.

		em.flush();
		em.clear();


		// then
		Posting findPosting = postingRepository.findById(posting.getId());
		assertEquals(TITLE, findPosting.getTitle());
		assertEquals(SUBTITLE, findPosting.getSubtitle());
		assertEquals(CONTENT, findPosting.getContent());
		assertEquals(BOARD_NAME, findPosting.getBoard().getName());
		assertEquals(Status.ACTIVE, findPosting.getStatus());
		assertEquals(USER_NICKNAME, findPosting.getUser().getNickname());
	}

	@Transactional
	@Test
	void update() {
		// given : 수정할 게시물 정보를 준다.
		final Long UPDATE_POSTING_ID = 1L; // id=1 게시물을 수정할 것이다. 따라서 주어짐.
		final String MODIFY_TITLE = "수정된 제목";
		final String MODIFY_SUBTITLE = "부제목을 수정해봤다.";
		final String MODIFY_CONTENT = "수정 수정 수정 수정 수정 00100010101";
		final String MODIFY_BOARD_NAME = "정보게시판"; // 원래 자유게시판 이었기에 정보게시판으로 바꿔줄 것이다.
		PostingRequestDTO updatePostingInfo = new PostingRequestDTO(); // 게시물 수정 정보
		updatePostingInfo.setTitle(MODIFY_TITLE);
		updatePostingInfo.setSubtitle(MODIFY_SUBTITLE);
		updatePostingInfo.setContent(MODIFY_CONTENT);
		updatePostingInfo.setBoardName(MODIFY_BOARD_NAME);

		Posting toBeUpdatedPosting = postingRepository.findById(UPDATE_POSTING_ID);
		Board toBeyUpdatedBoard = boardRepository.findByName(MODIFY_BOARD_NAME);


		// when
		toBeUpdatedPosting.update(updatePostingInfo);
		toBeUpdatedPosting.updateBoard(toBeyUpdatedBoard);

		em.flush(); // dirty checking 결과 update query 가 DB 로 날라가게 된다.
		em.clear();


		// then : 수정된 결과가 잘 반영됐는지 확인
		Posting result = postingRepository.findById(UPDATE_POSTING_ID);
		assertEquals(MODIFY_TITLE, result.getTitle());
		assertEquals(MODIFY_SUBTITLE, result.getSubtitle());
		assertEquals(MODIFY_CONTENT, result.getContent());
		assertEquals(MODIFY_BOARD_NAME, result.getBoard().getName());
	}

	@Transactional
	@Test
	void remove() {
		// given
		final Long REMOVE_POSTING_ID = 1L; // id=1 게시물을 삭제할 것이다. 따라서 주어짐.


		// when
		Posting toBeRemovedPosting = postingRepository.findById(REMOVE_POSTING_ID);
		postingRepository.remove(toBeRemovedPosting);

		em.flush(); // DELETE query 발생. 오류 발생.
		/**
		 * 오류 발생 => Posting Entity 의 preferenceList, comments field 에 cascade = CASCADE.REMOVE 걸어서 해결
		 */
		em.clear();


		// then
		Posting result = postingRepository.findById(REMOVE_POSTING_ID);
		assertNull(result); // 삭제했으니 존재하면 안된다.
	}
}