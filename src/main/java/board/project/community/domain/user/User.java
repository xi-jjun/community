package board.project.community.domain.user;

import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.Comment;
import board.project.community.domain.Posting;
import board.project.community.domain.preference.Preference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "Users")
@Entity
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String account;

	@Column
	private String password;

	@Column
	private String name;

	@Column
	private String nickname;

	@Column
	private int age;

	@Column
	private LocalDateTime createdDate;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user")
	private List<Posting> postings = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Preference> preferenceList = new ArrayList<>();

	/**
	 * User 생성
	 * @param userRequestDTO : 회원가입을 할 회원 정보가 담겨있는 객체
	 */
	public User(UserRequestDTO userRequestDTO) {
		this.name = userRequestDTO.getName();
		this.nickname = userRequestDTO.getNickname();
		this.account = userRequestDTO.getAccount();
		this.password = userRequestDTO.getPassword();
		this.age = userRequestDTO.getAge();
		this.createdDate = LocalDateTime.now();
		this.role = Role.USER;
	}

	/**
	 * ADMIN 에 의해서만 변경 가능. service layer 에서 구현 예정
	 * @param role : ADMIN, USER, BLOCK == 관리자, 사용자, 차단된사용자 3가지
	 */
	public void updateRole(Role role) {
		this.role = role;
	}

	/**
	 * 사용자 본인만 수정할 수 있는 권한이 주어져야 한다.
	 * @param userRequestDTO : 수정될 정보들이 담겨있는 객체
	 */
	public void update(UserRequestDTO userRequestDTO) {
		this.name = userRequestDTO.getName();
		this.nickname = userRequestDTO.getNickname();
		this.account = userRequestDTO.getAccount();
		this.password = userRequestDTO.getPassword();
		this.age = userRequestDTO.getAge();
	}
}
