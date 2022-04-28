package board.project.community.repository;

import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional(readOnly = true)
@SpringBootTest
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("사용자 정보 저장하기")
	@Transactional
	void save() {
		System.out.println("UserRepositoryTest.save");
		// given
		User toBeUser = User.UserBuilder()
				.name("김재준")
				.role(Role.USER)
				.credential("this is secret")
				.password("password123")
				.account("rlawowns97")
				.age(26)
				.createdDate(LocalDateTime.now())
				.nickname("공돌이")
				.build();

		// when
		userRepository.save(toBeUser);

		// then
		List<User> users = userRepository.findAll();

		for (User user : users) {
			assertEquals(toBeUser.getAccount(), user.getAccount());
			assertEquals(toBeUser.getAge(), user.getAge());
			assertEquals(toBeUser.getId(), user.getId());
			assertEquals(toBeUser.getName(), user.getName());
			assertEquals(toBeUser.getNickname(), user.getNickname());
			assertEquals(toBeUser.getCreatedDate(), user.getCreatedDate());
			assertEquals(toBeUser.getCredential(), user.getCredential());
			assertEquals(toBeUser.getPassword(), user.getPassword());
		}
	}

	@Test
	void findByNickname() {
		System.out.println("UserRepositoryTest.findByName");
		// given
		User toBeUser = User.UserBuilder()
				.name("김재준")
				.role(Role.USER)
				.credential("this is secret")
				.password("password123")
				.account("rlawowns97")
				.age(26)
				.createdDate(LocalDateTime.now())
				.nickname("공돌이")
				.build();

		userRepository.save(toBeUser);

		// when
		User findUser = userRepository.findByNickname(toBeUser.getNickname());

		// then
		assertEquals(toBeUser.getAccount(), findUser.getAccount());
		assertEquals(toBeUser.getAge(), findUser.getAge());
		assertEquals(toBeUser.getId(), findUser.getId());
		assertEquals(toBeUser.getName(), findUser.getName());
		assertEquals(toBeUser.getNickname(), findUser.getNickname());
		assertEquals(toBeUser.getCreatedDate(), findUser.getCreatedDate());
		assertEquals(toBeUser.getCredential(), findUser.getCredential());
		assertEquals(toBeUser.getPassword(), findUser.getPassword());
	}

	@Test
	void findByPK() {
		// given
		User toBeUser = User.UserBuilder()
				.name("김재준")
				.role(Role.USER)
				.credential("this is secret")
				.password("password123")
				.account("rlawowns97")
				.age(26)
				.createdDate(LocalDateTime.now())
				.nickname("공돌이")
				.build();

		userRepository.save(toBeUser);
		em.clear();

		// when
		User findUser = userRepository.findById(toBeUser.getId());

		// then
		assertEquals(toBeUser.getAccount(), findUser.getAccount());
		assertEquals(toBeUser.getAge(), findUser.getAge());
		assertEquals(toBeUser.getId(), findUser.getId());
		assertEquals(toBeUser.getName(), findUser.getName());
		assertEquals(toBeUser.getNickname(), findUser.getNickname());
		assertEquals(toBeUser.getCreatedDate(), findUser.getCreatedDate());
		assertEquals(toBeUser.getCredential(), findUser.getCredential());
		assertEquals(toBeUser.getPassword(), findUser.getPassword());
	}

	@Test
	@Transactional
	void findAllActiveUsers() {
		// given
		final int ACTIVE_USERS = 3;
		final int BLOCKED_USERS = 2;
		// active users
		for (int i = 0; i < ACTIVE_USERS; i++) {
			User toBeUser = User.UserBuilder()
					.name("김재준" + i)
					.role(Role.USER)
					.credential("this is secret")
					.password("password123")
					.account("rlawowns" + i)
					.age(26)
					.createdDate(LocalDateTime.now())
					.nickname("공돌이" + i)
					.build();
			userRepository.save(toBeUser);
		}

		// blocked users
		for (int i = 0; i < BLOCKED_USERS; i++) {
			User toBeUser = User.UserBuilder()
					.name("차단당한 사람" + i)
					.role(Role.BLOCKED)
					.credential("this is secret")
					.password("password123")
					.account("rlawowns" + (i + 5))
					.age(26)
					.createdDate(LocalDateTime.now())
					.nickname("공돌이" + (i + 5))
					.build();
			userRepository.save(toBeUser);
		}

		em.clear();

		// when
		List<User> allActiveUsers = userRepository.findAllActiveUsers();

		// then
		assertEquals(ACTIVE_USERS, allActiveUsers.size());
	}

	@Test
	@Transactional
	void findByAccount() {
		// given
		final String ACCOUNT = "rlawowns97";
		User toBeUser = User.UserBuilder()
				.name("김재준")
				.role(Role.USER)
				.credential("this is secret")
				.password("password123")
				.account(ACCOUNT)
				.age(26)
				.createdDate(LocalDateTime.now())
				.nickname("공돌이")
				.build();

		userRepository.save(toBeUser);

		// when
		User findUser = userRepository.findByAccount(ACCOUNT);

		// then
		assertEquals(toBeUser.getAccount(), findUser.getAccount());
		assertEquals(toBeUser.getAge(), findUser.getAge());
		assertEquals(toBeUser.getId(), findUser.getId());
		assertEquals(toBeUser.getName(), findUser.getName());
		assertEquals(toBeUser.getNickname(), findUser.getNickname());
		assertEquals(toBeUser.getCreatedDate(), findUser.getCreatedDate());
		assertEquals(toBeUser.getCredential(), findUser.getCredential());
		assertEquals(toBeUser.getPassword(), findUser.getPassword());
	}

	@Test
	void findAll() {
		// given
		final int ACTIVE_USERS = 3;
		final int BLOCKED_USERS = 2;
		final int ALL_USERS = ACTIVE_USERS + BLOCKED_USERS;
		// active users
		for (int i = 0; i < ACTIVE_USERS; i++) {
			User toBeUser = User.UserBuilder()
					.name("김재준" + i)
					.role(Role.USER)
					.credential("this is secret")
					.password("password123")
					.account("rlawowns" + i)
					.age(26)
					.createdDate(LocalDateTime.now())
					.nickname("공돌이" + i)
					.build();
			userRepository.save(toBeUser);
		}

		// blocked users
		for (int i = 0; i < BLOCKED_USERS; i++) {
			User toBeUser = User.UserBuilder()
					.name("차단당한 사람" + i)
					.role(Role.BLOCKED)
					.credential("this is secret")
					.password("password123")
					.account("rlawowns" + (i + 5))
					.age(26)
					.createdDate(LocalDateTime.now())
					.nickname("공돌이" + (i + 5))
					.build();
			userRepository.save(toBeUser);
		}

		// when
		List<User> users = userRepository.findAll();

		// then
		assertEquals(ALL_USERS, users.size());
	}

	@Test
	@Transactional
	void update() {
		// given
		User toBeUser = User.UserBuilder()
				.name("김재준")
				.role(Role.USER)
				.credential("this is secret")
				.password("password123")
				.account("rlawowns97")
				.age(26)
				.createdDate(LocalDateTime.now())
				.nickname("공돌이")
				.build();

//		userRepository.save(toBeUser);
		em.persist(toBeUser);
		em.flush();
		em.clear();

		User findUser = userRepository.findById(toBeUser.getId());
		findUser.setName("수정된 이름");

		em.flush();
		em.clear();

		// when
		userRepository.update(findUser);

		// then
		User result = userRepository.findById(toBeUser.getId());
		assertEquals(findUser.getName(), result.getName());
		System.out.println(result.getName());
	}

	@Test
	@Transactional
	void remove() {
		// given
		final int ACTIVE_USERS = 3;
		for (int i = 0; i < ACTIVE_USERS; i++) {
			User toBeUser = User.UserBuilder()
					.name("김재준" + i)
					.role(Role.USER)
					.credential("this is secret")
					.password("password123")
					.account("rlawowns" + i)
					.age(26)
					.createdDate(LocalDateTime.now())
					.nickname("공돌이" + i)
					.build();
			userRepository.save(toBeUser);
		}

		em.clear();

		// when
		final String toBeErasedUserNickname = "공돌이2";
		User findUser = userRepository.findByNickname(toBeErasedUserNickname);
		userRepository.remove(findUser);

		// then
		List<User> users = userRepository.findAll();
		assertEquals(ACTIVE_USERS - 1, users.size());
		for (User user : users) {
			System.out.println("user.getNickname() = " + user.getNickname());
		}
	}
}