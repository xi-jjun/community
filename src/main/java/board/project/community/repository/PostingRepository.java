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

	public Posting findByIdx(Long idx) {
		return em.find(Posting.class, idx);
	}

	public List<Posting> findAll() {
		return em.createQuery("select p from Posting p where p.status = :status", Posting.class)
				.setParameter("status", Status.ACTIVE)
				.getResultList();
	}

	public List<Posting> findByTitle(String title) {
		return em.createQuery("select p from Posting p where p.title = :title", Posting.class)
				.setParameter("title", title)
				.getResultList();
	}

	public List<Posting> findByNickname(String nickname) {
		return em.createQuery("select p from Posting p where p.user.nickname = :nickname", Posting.class)
				.setParameter("nickname", nickname)
				.getResultList();
	}

	@Transactional
	public void update(Posting posting) {
		Posting findPosting = findByIdx(posting.getIdx());

		findPosting.setTitle(posting.getTitle());
		findPosting.setSubtitle(posting.getSubtitle());
		findPosting.setContent(posting.getContent());
		findPosting.setStatus(posting.getStatus());
		findPosting.setUpdatedDate(LocalDateTime.now());
	}

	@Transactional
	public void deactivate(Long idx) {
		Posting updatePosting = findByIdx(idx);
		updatePosting.setStatus(Status.BLOCKED);
	}

	@Transactional
	public void remove(Long idx) {
		Posting posting = em.find(Posting.class, idx);
		em.remove(posting);
	}
}
