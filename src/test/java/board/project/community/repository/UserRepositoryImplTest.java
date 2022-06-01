package board.project.community.repository;

import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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

	/**
	 * 얘가 모든 @Test 가 실행되기 전에 실행되기 때문에 DB 에 같은 데이터가 배수로 쌓인다.
	 * 따라서 PK find test 를 못 만드는 상황에 처함.
	 */
	@Transactional
	@BeforeEach
	void makeUsers() {
		final String[] NAMES = {"김재현", "류도연", "윤상우", "박준영", "김근우"};
		for (int i = 1; i < 6; i++) {
			UserRequestDTO dto = new UserRequestDTO();
			dto.setAccount("userAccount" + i);
			dto.setPassword("password" + i);
			dto.setName(NAMES[i - 1]);
			dto.setNickname("Nickname" + i);
			dto.setAge(26 + i);

			User user = dto.toEntity();

			userRepository.save(user);
		}

		for (int i = 0; i < 2; i++) {
			UserRequestDTO dto = new UserRequestDTO();
			dto.setAccount("userAccountBB" + i);
			dto.setPassword("passwordBB" + i);
			dto.setName("이름"+i);
			dto.setNickname("blocked" + i);
			dto.setAge(16 + i);

			User user = dto.toEntity();
			user.updateRole(Role.BLOCKED);

			userRepository.save(user);
		}
	}

	@DisplayName("사용자 정보 저장하기")
	@Transactional
	@Test
	void save() {
		// given : 사용자가 회원가입하기 위해 입력한 정보는 UserRequestDTO 를 통해 정보가 전달 될 것
		UserRequestDTO dto = new UserRequestDTO();
		dto.setAccount("rlawowns97");
		dto.setPassword("password123");
		dto.setName("김재준");
		dto.setNickname("공돌이");
		dto.setAge(26);

		User toBeUser = dto.toEntity();


		// when
		userRepository.save(toBeUser);

		em.flush();
		em.clear();


		// then
		User findById = userRepository.findById(toBeUser.getId());
		User findByAccount = userRepository.findByAccount(toBeUser.getAccount());
		User findByNickname = userRepository.findByNickname(toBeUser.getNickname());

		assertEquals(findById.getNickname(), toBeUser.getNickname());
		assertEquals(findByAccount.getNickname(), toBeUser.getNickname());
		assertEquals(findByNickname.getNickname(), toBeUser.getNickname());
		assertEquals(findByNickname.getAccount(), toBeUser.getAccount());
	}

	@Test
	void findByNickname() {
		// given
		final String FIND_USER_NICKNAME = "Nickname2";


		// when
		User findUser = userRepository.findByNickname(FIND_USER_NICKNAME);

		System.out.println(findUser.getId());

		// then
		assertEquals(findUser.getAccount(), "userAccount2");
		assertEquals(findUser.getPassword(), "password2");
		assertEquals(findUser.getName(), "류도연");
		assertEquals(findUser.getNickname(), FIND_USER_NICKNAME);
		assertEquals(findUser.getAge(), 28);
	}

	@Test
	void findAllActiveUsers() {
		// given
		final int BLOCKED_USER_NUMBER = 2;


		// when
		List<User> users = userRepository.findAll();
		List<User> allActiveUsers = userRepository.findAllActiveUsers();


		// then
		System.out.println(users.size());
		assertEquals(users.size() - BLOCKED_USER_NUMBER, allActiveUsers.size());
	}

	@Test
	void findByAccount() {
		// given
		final String FIND_ACCOUNT = "userAccount4";


		// when
		User findUserByAccount = userRepository.findByAccount(FIND_ACCOUNT);


		// then
		assertEquals(findUserByAccount.getAccount(), FIND_ACCOUNT);
		assertEquals(findUserByAccount.getPassword(), "password4");
		assertEquals(findUserByAccount.getName(), "박준영");
		assertEquals(findUserByAccount.getNickname(), "Nickname4");
		assertEquals(findUserByAccount.getAge(), 30);
	}

	@Test
	void findAll() {
		// given
		final int ALL_USERS = 7;


		// when
		List<User> users = userRepository.findAll();


		// then
		assertEquals(ALL_USERS, users.size());
	}

	@Transactional
	@Test
	void updateUserInfo() {
		// given : update 할 정보를 client 에게 받아서 서버로 가져온다. 그 때 UserRequestDTO 에 담아서 가져온다.
		UserRequestDTO updateDto = new UserRequestDTO();
		updateDto.setAccount("rlawowns97");
		updateDto.setPassword("password123");
		updateDto.setName("수정된 이름");
		updateDto.setNickname("공돌이");
		updateDto.setAge(26);

		final String USER_NICKNAME_IN_SESSION = "Nickname2";

		// when
		User findUser = userRepository.findByNickname(USER_NICKNAME_IN_SESSION);
		findUser.update(updateDto);
		em.flush();
		em.clear();


		// then
		User result = userRepository.findById(findUser.getId());
		assertEquals(findUser.getPassword(), result.getPassword());
		assertEquals(findUser.getAge(), result.getAge());
		assertEquals(findUser.getName(), result.getName());
		assertEquals(findUser.getNickname(), result.getNickname());
		assertEquals(findUser.getAccount(), result.getAccount());
		assertEquals(findUser.getRole(), result.getRole());
		assertEquals(findUser.getCreatedDate(), result.getCreatedDate());
	}

	@Transactional
	@Test
	void remove() {
		// given
		final String REMOVED_USER_NICKNAME = "Nickname3";


		// when
		User findUser = userRepository.findByNickname(REMOVED_USER_NICKNAME);
		Long findId = findUser.getId();
		userRepository.remove(findUser);

		em.flush();
		em.clear();


		// then
		User removedUser = userRepository.findById(findId);
		assertNull(removedUser);
	}
}