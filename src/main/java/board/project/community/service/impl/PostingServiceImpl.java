package board.project.community.service.impl;

import board.project.community.controller.ResponseDTO;
import board.project.community.controller.dto.request.PostingRequestDTO;
import board.project.community.controller.dto.response.PostingResponseDTO;
import board.project.community.domain.Posting;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import board.project.community.repository.PostingRepository;
import board.project.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostingServiceImpl implements PostingService {
	private final PostingRepository postingRepository;

	@Transactional
	public void posting(Posting posting) {
		postingRepository.save(posting);
	}

	public List<Posting> findAllPostings() {
		return postingRepository.findAll();
	}

	public Posting findByIdx(Long id) {
		return postingRepository.findById(id);
	}

	/**
	 * DTO to Entity mapping 부터 하자!!!
	 * 그리고 controller 완성하자
	 * @param posting
	 */
//	@Transactional
//	public void updatePosting(Posting posting) {
//		postingRepository.update(posting);
//	}

	@Transactional
	public void remove(Posting posting) {
		postingRepository.remove(posting);
	}

	@Override
	public ResponseDTO addPosting(PostingRequestDTO requestDTO, Authentication authentication) {
		return null;
	}

	@Override
	public ResponseDTO updatePosting(PostingRequestDTO requestDTO, Long postingId, Authentication authentication) {
		return null;
	}

	@Override
	public ResponseDTO removePosting(Long postingId, Authentication authentication) {
		return null;
	}

	@Override
	public ResponseDTO changeStatus(Long postingId, Authentication authentication) {
		return null;
	}

	@Override
	public PostingResponseDTO getPostingDetail(Long postingId, Authentication authentication) {
		return null;
	}

	@Override
	public List<PostingResponseDTO> getPostingListByBoard(String boardName, Authentication authentication) {
		return null;
	}

	@Override
	public List<PostingResponseDTO> getPostingListByUser(Long userId, Authentication authentication) {
		return null;
	}
}
