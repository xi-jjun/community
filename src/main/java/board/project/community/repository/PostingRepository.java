package board.project.community.repository;

import board.project.community.domain.posting.Posting;
import board.project.community.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class PostingRepository {
	private final EntityManager em;

	@Transactional
	public void save(Posting posting) {
		em.persist(posting);
	}

	public Posting findById(Long id) {
		return em.find(Posting.class, id);
	}

	public List<Posting> findAll() {
		return em.createQuery("select p from Posting p where p.status = :status", Posting.class)
				.setParameter("status", Status.ACTIVE)
				.getResultList();
	}

	@Transactional
	public void update(Posting posting) {
		Posting findPosting = findById(posting.getId());

		findPosting.setTitle(posting.getTitle());
		findPosting.setSubtitle(posting.getSubtitle());
		findPosting.setContent(posting.getContent());
		findPosting.setStatus(posting.getStatus());
		findPosting.setUpdatedDate(LocalDateTime.now());
	}

	@Transactional
	public void deactivate(Long id) {
		Posting updatePosting = findById(id);
		updatePosting.setStatus(Status.BLOCKED);
	}

	@Transactional
	public void remove(Long id) {
		Posting posting = em.find(Posting.class, id);
		em.remove(posting);
	}
}
