package board.project.community.repository;

import board.project.community.controller.dto.request.BoardRequestCreateDTO;
import board.project.community.domain.Status;
import board.project.community.domain.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional(readOnly = true)
@SpringBootTest
class BoardRepositoryTest {
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private EntityManager em;

	@Transactional
	@BeforeEach
	void makeBoards() {
		// given
		final String[] BOARD_NAMES = {"자유게시판", "정보게시판", "공지게시판"};
		final BoardRequestCreateDTO[] dtos = new BoardRequestCreateDTO[3];
		for (int i = 0; i < BOARD_NAMES.length; i++) {
			dtos[i] = new BoardRequestCreateDTO();
			dtos[i].setBoardName(BOARD_NAMES[i]);

			Board board = dtos[i].toEntity();
			boardRepository.addBoard(board);
		}
	}

	@Transactional
	@AfterEach
	void clear() {
		List<Board> boards = boardRepository.findAll();
		for (Board board : boards) {
			boardRepository.remove(board.getName());
		}
	}

	@Transactional
	@Test
	void addBoard() {
		// given
		final String BOARD_NAME = "추가할게시판";
		// @RequestBody 로 받아오는 DTO 객체
		final BoardRequestCreateDTO createDTO = new BoardRequestCreateDTO();
		createDTO.setBoardName(BOARD_NAME);
		Board board = new Board(createDTO);


		// when
		boardRepository.addBoard(board);

		em.flush();
		em.clear();


		// then
		Board findBoard = boardRepository.findByName(BOARD_NAME);
		assertEquals(BOARD_NAME, findBoard.getName());
	}

	@Test
	void findAllBoards() {
		// given
		// before each 에 있다.
		final String[] BOARD_NAMES = {"자유게시판", "정보게시판", "공지게시판"};


		// when
		List<Board> boards = boardRepository.findAll();


		// then
		int idx = 0;
		for (Board board : boards) {
			System.out.println(board.getId() + " " + board.getName());
			assertEquals(BOARD_NAMES[idx++] , board.getName());
			assertEquals(Status.ACTIVE, board.getStatus());
		}
	}

	@Test
	void findByName() {
		// given
		final String[] BOARD_NAMES = {"자유게시판", "정보게시판", "공지게시판"};


		// when
		// id=2 인 board entity 를 조회하는 것
		Board findBoard = boardRepository.findByName(BOARD_NAMES[1]);


		// then
		assertEquals(findBoard.getName(), BOARD_NAMES[1]);
		assertEquals(findBoard.getStatus(), Status.ACTIVE);
	}

	@Transactional
	@Test
	void remove() {
		// given
		final String[] BOARD_NAMES = {"자유게시판", "정보게시판", "공지게시판"};


		// when
		boardRepository.remove(BOARD_NAMES[1]); // "정보게시판" 삭제

		em.flush();
		em.clear();


		// then
		List<Board> boardList = boardRepository.findAll();
		assertEquals(boardList.size(), BOARD_NAMES.length - 1);
		assertEquals(boardList.get(0).getName(), BOARD_NAMES[0]);
		assertEquals(boardList.get(1).getName(), BOARD_NAMES[2]);
	}

	/**
	 * update method 는 service 에서 만들기 위해
	 */
}