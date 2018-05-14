package youngScholar.model.account;

import youngScholar.entity.User;
import youngScholar.model.ModelFromEntity;
import lombok.Data;

@Data
public class InvitationCodeOutput implements ModelFromEntity<User, InvitationCodeOutput>
{
	private String invitationCode;
	private int invitationCount;

	@Override
	public InvitationCodeOutput fromEntity(User user)
	{
		invitationCode = user.getInvitationCode();
		invitationCount = user.getInvitationCount();
		return this;
	}
}
