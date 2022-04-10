package board.project.community.domain;

import board.project.community.domain.posting.Posting;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Setter // temp
@Getter
@Entity
public class Board {
	@Id
	@Column(name = "board_idx")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;

	@Column(name = "board_name")
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(mappedBy = "board")
	private List<Posting> postings = new ArrayList<>();
}
