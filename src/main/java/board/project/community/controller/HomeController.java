package board.project.community.controller;

import board.project.community.domain.Status;
import board.project.community.domain.posting.Posting;
import board.project.community.domain.posting.PostingDTO;
import board.project.community.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class HomeController {
	private final PostingService postingService;

	@GetMapping("/")
	public String home() {
		make();
		return "home";
	}

	/**
	 * make the dummy data for testing
	 */
	public void make() {
		for (int i = 0; i < 6; i++) {
			Posting posting = Posting.PostingBuilder()
					.title("title " + i)
					.subtitle("subtitle " + i)
					.content("content " + i)
					.status(Status.ACTIVE)
					.createdDate(LocalDateTime.now())
//					.user() // 아직 UserRepository 를 만들지 않았다
//					.board() // 아직 BoardRepository 를 만들지 않았다
					.build();
			postingService.posting(posting);
		}
	}
}
