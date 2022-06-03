package board.project.community.domain.user;

import board.project.community.controller.dto.request.UserRequestDTO;
import board.project.community.domain.Comment;
import board.project.community.domain.Posting;
import board.project.community.domain.preference.Preference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	@NotNull(message = "account 는 null 일 수 없습니다.")
	@Size(min = 3, max = 20, message = "account 는 3~20 자 사이여야 합니다.")
	private String account;

	@Column
	@NotNull(message = "password 는 null 일 수 없습니다.")
	private String password;

	@Column
	@NotNull(message = "name 은 null 이 될 수 없습니다.")
	private String name;

	@Column
	@NotNull
	@Size(min = 1, max = 10, message = "nickname 은 1~10 자 사이여야 합니다.")
	private String nickname;

	@Column
	private int age;

	@Column
	private LocalDateTime createdDate;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Posting> postings = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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
		this.role = isNone(userRequestDTO) ? Role.USER : Role.from(userRequestDTO.getRole());
		this.createdDate = LocalDateTime.now();
	}

	private boolean isNone(UserRequestDTO userRequestDTO) {
		return userRequestDTO.getRole() == null || userRequestDTO.getRole().isBlank();
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
