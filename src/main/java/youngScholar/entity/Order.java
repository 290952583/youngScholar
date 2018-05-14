package youngScholar.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Orders")
public class Order implements BelongUser
{
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@GeneratedValue(generator = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Timestamp time;

	@Column(nullable = false)
	private int amount;

	@ManyToOne
	@JoinColumn(name = "mission_id")
	private Mission mission;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	public Order()
	{
	}

	public Order(User user, int amount, Mission mission, Type type)
	{
		this(user, amount, type);
		this.mission = mission;
	}

	public Order(User user, int amount, Type type)
	{
		this.user = user;
		this.amount = amount;
		this.type = type;
	}

	public enum Type
	{
		CHARGE,
		CHARGE_SUCCEEDED,
		TRANSFER,
		TRANSFER_SUCCEEDED,
		TRANSFER_FAILED,
		RELEASE,
		FINISH,
	}
}
