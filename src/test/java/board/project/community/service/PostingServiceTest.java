package board.project.community.service;

import static org.junit.jupiter.api.Assertions.*;

import board.project.community.domain.posting.Posting;
import board.project.community.domain.Status;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class PostingServiceTest {
	@Autowired
	private PostingService postingService;

	@Autowired
	private EntityManager em;

	@BeforeEach
	void createDummy() {
		Posting posting = getPosting("title", "subTitle", "contentttttt", Status.ACTIVE, LocalDateTime.now());
		Posting posting1 = getPosting("title1", "subTitle1", "contentttttt1", Status.ACTIVE, LocalDateTime.now());
		Posting posting2 = getPosting("title2", "subTitle2", "contentttttt2", Status.ACTIVE, LocalDateTime.now());
		Posting posting3 = getPosting("title3", "subTitle3", "contentttttt3", Status.ACTIVE, LocalDateTime.now());
		postingService.posting(posting);
		postingService.posting(posting1);
		postingService.posting(posting2);
		postingService.posting(posting3);
	}

	@Test
	void saveAndFind() {
		// given
		Posting posting = getPosting("title11", "subtitle 11",
				"content 11", Status.BLOCKED, LocalDateTime.now());
		postingService.posting(posting);

		// when
		List<Posting> postings = postingService.findByTitle(posting.getTitle());
		Posting findPosting = postings.get(0);

		// then
		assertEquals(posting.getTitle(), findPosting.getTitle());
		assertEquals(posting.getSubtitle(), findPosting.getSubtitle());
		assertEquals(posting.getContent(), findPosting.getContent());
		assertEquals(posting.getStatus(), findPosting.getStatus());
		assertEquals(posting.getCreatedDate(), findPosting.getCreatedDate());
	}

	private Posting getPosting(String title, String subtitle, String content, Status status, LocalDateTime time) {
		Posting posting = new Posting();
		posting.setTitle(title);
		posting.setSubtitle(subtitle);
		posting.setContent(content);
		posting.setStatus(status);
		posting.setCreatedDate(time);
		return posting;
	}

	@Test
	void findAll() {
		List<Posting> postings = postingService.findAllPostings();
		for (Posting posting : postings) {
			System.out.println(posting.getTitle());
		}
	}

	@Test
	void remove() {
		final String TO_BE_REMOVED_TITLE = "title";
		// when : @Before 에서 생성된 title 이름에 관한 Posting Entity 를 삭제.
		List<Posting> postings = postingService.findByTitle(TO_BE_REMOVED_TITLE);
		for (Posting posting : postings) {
			postingService.remove(posting);
		}

		// then : 그 후 남은 Posting Entity 를 모두 조회하여 같은 이름이 존재하는지 확인
		List<Posting> allPostings = postingService.findAllPostings();
		for (Posting posting : allPostings) {
			assertNotEquals(posting.getTitle(), TO_BE_REMOVED_TITLE);
		}
	}

	@Test
	void deactivate() {
		// given : ADMIN user
		User admin = new User();
		admin.setId("admin_1123");
		admin.setAge(26);
		admin.setRole(Role.ADMIN);
		admin.setName("Kim Jae Jun");
		admin.setNickname("Gong Rock E");
		admin.setPassword("1234");

		postingService.deactivate(admin, 1L);

		Posting findPosting = postingService.findByIdx(1L);

		assertEquals(Status.BLOCKED, findPosting.getStatus());
	}
}
