package board.project.community.domain.preference;

import board.project.community.domain.Comment;
import board.project.community.domain.posting.Posting;
import board.project.community.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class Preference {
	@Id
	@Column(name = "preference_idx")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column
	@Enumerated(EnumType.STRING)
	private PreferenceType preferenceType;

	@Column
	@Enumerated(EnumType.STRING)
	private Type type;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_idx")
	private User user;

	@ManyToOne(targetEntity = Posting.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "posting_idx")
	private Posting posting;

	@ManyToOne(targetEntity = Comment.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_idx")
	private Comment comment;
}
