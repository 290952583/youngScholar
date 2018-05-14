package youngScholar.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Data
@Entity
public class Mission implements BelongUser
{
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 5120)
	private String description;

	@Column(nullable = false)
	@Length(min = 2)
	private String address;

	@Column(nullable = false)
	@Min(0)
	private int price;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Timestamp time;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private Status status = Status.WAIT;

	@ManyToOne
	@JoinColumn(name = "accept_user_id")
	private User acceptUser;

	@Column
	private Timestamp acceptTime;

    public enum Status
	{
		WAIT,
		PROCESSING,
		CANCEL,
		FINISH,
	}
}
