package board.project.community.repository;

import board.project.community.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class BoardRepository {
	private final EntityManager em;

	@Transactional
	public void addBoard(Board board) {
		em.persist(board);
	}

	public List<Board> findAll() {
		return em.createQuery("select b from Board b", Board.class)
				.getResultList();
	}

	public Board findByName(String name) {
		return em.createQuery("select b from Board b where b.name=:name", Board.class)
				.setParameter("name", name)
				.getSingleResult();
	}

	@Transactional
	public void remove(String name) {
		Board board = findByName(name);
		em.remove(board);
	}
}
