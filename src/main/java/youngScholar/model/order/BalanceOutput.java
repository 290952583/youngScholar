package youngScholar.model.order;

import youngScholar.entity.User;
import youngScholar.model.ModelFromEntity;
import lombok.Data;

@Data
public class BalanceOutput implements ModelFromEntity<User, BalanceOutput>
{
	private int balance;

	@Override
	public BalanceOutput fromEntity(User user)
	{
		balance = user.getBalance();
		return this;
	}
}
