package board.project.community.repository.impl;

import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.user.Role;
import board.project.community.domain.user.User;
import board.project.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
	private final EntityManager em;

	@Transactional
	public void save(User user) {
		em.persist(user);
	}

	@Transactional
	@Override
	public void update(UserRequestDTO requestDTO, Long id) {
		User updateUser = em.find(User.class, id);
		updateUser.update(requestDTO);
	}

	@Transactional
	public void remove(User user) {
		em.remove(user);
	}

	public User findById(Long id) {
		return em.find(User.class, id);
	}

	public List<User> findActiveUsers() {
		return em.createQuery("select u from User u where u.role = :role", User.class)
				.setParameter("role", Role.USER)
				.getResultList();
	}

	public User findByAccount(String account) {
		try {
			return em.createQuery("select u from User as u where u.account = :account", User.class)
					.setParameter("account", account)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("Account '{}' is not existed", account);
			return null;
		}
	}

	public User findByNickname(String nickname) {
		try {
			return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
					.setParameter("nickname", nickname)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("Nickname '{}' is not existed", nickname);
			return null;
		}
	}

	public List<User> findAll() {
		return em.createQuery("select u from User as u", User.class)
				.getResultList();
	}
}

