package board.project.community.controller;

import board.project.community.domain.posting.Posting;
import board.project.community.domain.posting.PostingDTO;
import board.project.community.domain.Status;
import board.project.community.domain.posting.PostingSearchDTO;
import board.project.community.service.PostingService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostingController {
	private final PostingService postingService;

	@GetMapping("/postings")
	public List<PostingSearchDTO> findPostings() {
		List<Posting> postings = postingService.findAllPostings();
		List<PostingSearchDTO> list = postings.stream()
				.map(PostingSearchDTO::new)
				.collect(Collectors.toList());
		return list;
	}

	@GetMapping("/postings/{postingIdx}")
	public PostingDTO findPosting(@PathVariable("postingIdx") String postingIdx) {
		Long idx = Long.parseLong(postingIdx);
		Posting findPosting = postingService.findByIdx(idx);
		return Posting.convertToDTO(findPosting);
	}

	/**
	 * DTO to Entity mapping 부터 하자!!!
	 * 그리고 controller 완성하자
	 */
//	@PatchMapping("/postings/{postingsIdx}")
//	public String updatePosting(@RequestBody PostingDTO posting) {
//		postingService.updatePosting(posting);
//	}

}
