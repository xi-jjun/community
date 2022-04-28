package board.project.community.repository;

import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class UserRepository {
	private final EntityManager em;

	@Transactional
	public void save(User user) {
		em.persist(user);
	}

	public User findById(Long id) {
		return em.find(User.class, id);
	}

	public List<User> findAllActiveUsers() {
		return em.createQuery("select u from User u where u.role = :role", User.class)
				.setParameter("role", Role.USER)
				.getResultList();
	}

	public User findByAccount(String account) {
		return em.createQuery("select u from User as u where u.account = :account", User.class)
				.setParameter("account", account)
				.getSingleResult();
	}

	public User findByNickname(String nickname) {
		return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
				.setParameter("nickname", nickname)
				.getSingleResult();
	}

	public List<User> findAll() {
		return em.createQuery("select u from User as u", User.class)
				.getResultList();
	}

	@Transactional
	public void update(User user) {
		User findUser = findById(user.getId());

		findUser.setNickname(user.getNickname());
		findUser.setName(user.getName());
		findUser.setAge(user.getAge());
		findUser.setAccount(user.getAccount());
		findUser.setPassword(user.getPassword());
	}

	@Transactional
	public void remove(User user) {
		User findUser = em.find(User.class, user.getId());
		em.remove(findUser);
	}
}

