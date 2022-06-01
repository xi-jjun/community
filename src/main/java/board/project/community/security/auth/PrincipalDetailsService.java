package board.project.community.security.auth;

import board.project.community.domain.user.User;
import board.project.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
		User userEntity = userRepository.findByAccount(account);

		if (userEntity == null) {
			throw new UsernameNotFoundException("Username not founded");
		}

		return new PrincipalDetails(userEntity);
	}
}
