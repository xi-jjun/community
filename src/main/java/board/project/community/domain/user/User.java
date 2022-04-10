package board.project.community.domain.user;

import board.project.community.domain.Status;
import board.project.community.domain.user.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter // temp
@Getter
@Entity
public class User {
	@Id
	@Column(name = "user_idx")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column
	private String id;

	@Column
	private String password;

	@Column
	private String name;

	@Column
	private String nickname;

	@Column
	private int age;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * 필요없어짐. 왜? ROLE class 에서 BLOCKED 를 판단해주기 때문.
	 */
//	@Column
//	@Enumerated(EnumType.STRING)
//	private Status status;
}
