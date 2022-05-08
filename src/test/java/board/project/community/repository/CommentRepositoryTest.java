package board.project.community.repository;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import board.project.community.controller.dto.request.CommentRequestCreateDTO;
import board.project.community.controller.dto.request.CommentRequestUpdateDTO;
import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.Board;
import board.project.community.domain.Comment;
import board.project.community.domain.Posting;
import board.project.community.domain.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(readOnly = true)
@SpringBootTest
class CommentRepositoryTest {
	@Autowired
	private EntityManager em;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostingRepository postingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Transactional
	@BeforeAll
	void make() {
		// make user
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setName("김재준");
		userDTO.setNickname("공돌이는공돌공돌해");
		userDTO.setAccount("rlawowns97");
		userDTO.setPassword("pw123");
		userDTO.setAge(26);

		User user = userDTO.toEntity();
		userRepository.save(user);

		// make board
		BoardRequestCreateDTO boardDTO = new BoardRequestCreateDTO();
		boardDTO.setBoardName("자유게시판");

		Board board = boardDTO.toEntity();
		boardRepository.addBoard(board);

		// make posting
		PostingRequestDTO postingDTO = new PostingRequestDTO();
		postingDTO.setContent("내용입니다. 내용인데? 내용입니다. 나의 게시판 이것은. 했습니다 노력. 매우 하드하게");
		postingDTO.setTitle("제목입니다");
		postingDTO.setSubtitle("부제목이랄까요");
		postingDTO.setBoardName(board.getName());

		Posting posting = postingDTO.toEntity();
		posting.updateBoard(board); // 게시판과 연관관계 만들어준다.
		posting.initUser(user); // 사용자와 연관관계 만들어준다.
		postingRepository.save(posting);

		// make comment
		CommentRequestCreateDTO commentDTO = new CommentRequestCreateDTO();
		commentDTO.setComment("이게 댓글 본문입니다. 아 ㅋㅋ 댓글 그렇게 구현하는거 아닌데 아 ㅋㅋ");
		commentDTO.setGroupIdx(1); // 1번째 그룹의
		commentDTO.setLevel(0); // groupIdx 의 대빵 댓글

		Comment comment = commentDTO.toEntity();
		comment.initUser(user);
		comment.initPosting(posting);
		commentRepository.save(comment);
	}

	@Test
	void findComment() {
		Comment comment = commentRepository.findById(1L);

		assertEquals(comment.getComment(), "이게 댓글 본문입니다. 아 ㅋㅋ 댓글 그렇게 구현하는거 아닌데 아 ㅋㅋ");
		assertEquals(comment.getGroupIdx(), 1);
		assertEquals(comment.getLevel(), 0);
		assertEquals(comment.getPosting().getTitle(), "제목입니다");
		assertEquals(comment.getUser().getNickname(), "공돌이는공돌공돌해");
	}

	@Transactional
	@Test
	void addComment() {
		// given
		Posting findPosting = postingRepository.findById(1L);
		User findUser = userRepository.findById(1L);

		CommentRequestCreateDTO commentDTO = new CommentRequestCreateDTO();
		commentDTO.setComment("대빵댓글2");
		commentDTO.setGroupIdx(2);
		commentDTO.setLevel(0);

		Comment comment = commentDTO.toEntity();
		comment.initUser(findUser);
		comment.initPosting(findPosting);
		commentRepository.save(comment);

		em.flush();
		em.clear();


		// when
		Posting posting = postingRepository.findById(1L);
		List<Comment> comments = posting.getComments();


		// then
		int idx = 0;
		final String[] commentContents = {"이게 댓글 본문입니다. 아 ㅋㅋ 댓글 그렇게 구현하는거 아닌데 아 ㅋㅋ", "대빵댓글2"};
		for (Comment cmt : comments) {
			assertEquals(cmt.getComment(), commentContents[idx++]);
			System.out.println("cmt.getComment() = " + cmt.getComment());
		}
	}

	@Transactional
	@Test
	void remove() {
		// given
		final Long REMOVE_COMMENT_ID = 1L;


		// when
		Comment findComment = commentRepository.findById(REMOVE_COMMENT_ID);
		commentRepository.remove(findComment);

		em.flush();
		em.clear();


		// then
		Comment result = commentRepository.findById(REMOVE_COMMENT_ID);
		assertNull(result);
	}

	@Transactional
	@DisplayName("이미 만들어진 댓글 1번에 대댓글 작성 테스트")
	@Test
	void addReComment() {
		// 댓글을 쓴 사람의 사용자 정보
		UserRequestDTO userDto = new UserRequestDTO();
		userDto.setName("박명수");
		userDto.setNickname("익명의댓글작성자");
		userDto.setAccount("accountPark");
		userDto.setPassword("1234");
		userDto.setAge(39);

		User commenter = userDto.toEntity();
		userRepository.save(commenter);

		// 댓글을 쓸 게시물의 정보
		Posting commentedPosting = postingRepository.findById(1L);

		// given : level 은 front 에서 처리. ex) '대댓글' 버튼 눌러서 달면, 해당 groupIdx 와 level=1 로 설정해서 서버에 전달.
		CommentRequestCreateDTO dto = new CommentRequestCreateDTO();
		dto.setComment("이거 대댓글인데 대댓글 그렇게 적는거 아닌데 아 ㅋㅋ");
		dto.setGroupIdx(1);
		dto.setLevel(1);

		Comment comment = dto.toEntity(); // 1. 사용자가 작성한 댓글 정보를 entity 로 변환
		comment.initUser(commenter); // 2. 사용자 정보를 댓글 entity 에 연관시켜 준다.
		comment.initPosting(commentedPosting); // 3. 현재 게시물에 댓글을 작성하고 있기에 연관시켜 준다.

		commentRepository.save(comment); // 4. save

		em.flush();
		em.clear();


		// when
		Posting posting = postingRepository.findById(1L);


		// then
		List<Comment> comments = posting.getComments();
		assertEquals(2, comments.size());
		for (Comment cmt : comments) {
			System.out.println("작성자 : " + cmt.getUser().getNickname());
			System.out.println("내용 : " + cmt.getComment());
		}
	}

	@Transactional
	@Test
	void updateComment() {
		// given : 수정하기 위해 내용을 다시 보낸다. 그 정보가 주어짐
		final Long UPDATE_COMMENT_ID = 1L;
		CommentRequestUpdateDTO updateDTO = new CommentRequestUpdateDTO();
		updateDTO.setComment("나 댓글 바꿀건데 아 ㅋㅋ");


		// when
		Comment findComment = commentRepository.findById(UPDATE_COMMENT_ID);
		findComment.update(updateDTO);

		em.flush();
		em.clear();


		// then
		Comment comment = commentRepository.findById(UPDATE_COMMENT_ID);
		assertEquals("나 댓글 바꿀건데 아 ㅋㅋ", comment.getComment());
	}
}