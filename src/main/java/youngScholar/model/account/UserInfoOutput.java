package youngScholar.model.account;

import youngScholar.entity.User;
import youngScholar.model.ModelFromEntity;
import lombok.Data;

@Data
public class UserInfoOutput implements ModelFromEntity<User, UserInfoOutput>
{
	private String id;
	private String username;
	private String mobile;
	private String idCard;
	private String name;
	private char sex;
	private User.AuthStatus authStatus;
	private School school;

	@Override
	public UserInfoOutput fromEntity(User user)
	{
		id = user.getId();
		username = user.getUsername();
		mobile = user.getMobile();
		idCard = user.getIdCard();
		name = user.getName();
		sex = user.getSex();
		authStatus = user.getAuthStatus();
		school = new School().fromEntity(user.getSchool());
		return this;
	}

	@Data
	public static class School implements ModelFromEntity<youngScholar.entity.School, School>
	{
		private String id;
		private String school;

		@Override
		public School fromEntity(youngScholar.entity.School school)
		{
			this.id = school.getId();
			this.school = school.getSchool();
			return this;
		}
	}
}
