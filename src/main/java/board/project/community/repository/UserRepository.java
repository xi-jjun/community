package board.project.community.repository;

import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.user.User;

import java.util.List;

public interface UserRepository {
	public void save(User user);

	public void update(UserRequestDTO requestDTO, Long id);

	public void remove(User user);

	public User findById(Long id);

	public User findByAccount(String account);

	public User findByNickname(String nickname);

	public List<User> findActiveUsers();

	public List<User> findAll();
}
