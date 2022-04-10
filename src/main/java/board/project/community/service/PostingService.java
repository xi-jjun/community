package board.project.community.service;

import board.project.community.domain.posting.Posting;
import board.project.community.domain.posting.PostingDTO;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import board.project.community.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostingService {
	private final PostingRepository postingRepository;

	@Transactional
	public void posting(Posting posting) {
		postingRepository.save(posting);
	}

	public List<Posting> findAllPostings() {
		return postingRepository.findAll();
	}

	public List<Posting> findByTitle(String title) {
		return postingRepository.findByTitle(title);
	}

	public List<Posting> findByNickname(String nickname) {
		return postingRepository.findByNickname(nickname);
	}

	public Posting findByIdx(Long idx) {
		return postingRepository.findByIdx(idx);
	}

	/**
	 * DTO to Entity mapping 부터 하자!!!
	 * 그리고 controller 완성하자
	 * @param posting
	 */
	@Transactional
	public void updatePosting(Posting posting) {
		postingRepository.update(posting);
	}

	@Transactional
	public void remove(Posting posting) {
		postingRepository.remove(posting.getIdx());
	}

	@Transactional
	public void deactivate(User user, Long postingIdx) {
		if (user.getRole() == Role.ADMIN) {
			postingRepository.deactivate(postingIdx);
		}
	}
}
