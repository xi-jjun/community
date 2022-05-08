package board.project.community.repository;

import board.project.community.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class CommentRepository {
	private final EntityManager em;

	@Transactional
	public void save(Comment comment) {
		em.persist(comment);
	}

	public Comment findById(Long id) {
		return em.find(Comment.class, id);
	}

	public List<Comment> findAll() {
		return em.createQuery("select c from Comment c", Comment.class)
				.getResultList();
	}

	public List<Comment> findByPostingId(Long postingId) {
		return em.createQuery("select c from Comment c where c.posting.id=:postingId", Comment.class)
				.setParameter("postingId", postingId)
				.getResultList();
	}

	@Transactional
	public void remove(Comment comment) {
		em.remove(comment);
	}
}
