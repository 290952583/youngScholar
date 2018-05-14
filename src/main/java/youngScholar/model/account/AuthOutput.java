package youngScholar.model.account;

import youngScholar.entity.User;
import youngScholar.model.ModelFromEntity;
import lombok.Data;

@Data
public class AuthOutput implements ModelFromEntity<User, AuthOutput>
{
	private User.AuthStatus auth;

	@Override
	public AuthOutput fromEntity(User user)
	{
		auth = user.getAuthStatus();
		return this;
	}
}
