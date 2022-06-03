package board.project.community.repository.impl;

import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.domain.Posting;
import board.project.community.domain.Status;
import board.project.community.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class PostingRepositoryImpl implements PostingRepository {
	private final EntityManager em;

	@Transactional
	public void save(Posting posting) {
		em.persist(posting);
	}

	@Override
	public void update(PostingRequestDTO requestDTO, Long id) {

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
	public void deactivate(Long id) {
		Posting updatePosting = findById(id);
		updatePosting.updateStatus(Status.BLOCKED);
	}

	/**
	 * 찾는 행위를 service layer 에 위임하자. 이 method 는 '삭제' 만 하는 method 이다.
	 * @param posting - 삭제할 Posting entity
	 */
	@Transactional
	public void remove(Posting posting) {
		em.remove(posting);
	}
}
