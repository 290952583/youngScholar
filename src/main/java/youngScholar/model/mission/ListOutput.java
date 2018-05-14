package youngScholar.model.mission;

import youngScholar.entity.Mission;
import youngScholar.model.ModelFromEntity;
import youngScholar.model.ModelFromEntityList;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ListOutput implements ModelFromEntityList<Mission, ListOutput>
{
	private String id;
	private User user;
	private String title;
	private String description;
	private String address;
    private int price;
	private Timestamp time;
    private Mission.Status status;
	private User acceptUser;
	private Timestamp acceptTime;

	@Override
	public ListOutput fromEntity(Mission mission)
	{
		id = mission.getId();
		user = new User().fromEntity(mission.getUser());
		title = mission.getTitle();
		description = mission.getDescription();
		address = mission.getAddress();
		price = mission.getPrice();
		time = mission.getTime();
		status = mission.getStatus();
		if (mission.getAcceptUser() != null)
		{
			acceptUser = new User().fromEntity(mission.getAcceptUser());
			acceptTime = mission.getAcceptTime();
		}
		return this;
	}

	@Data
	public class User implements ModelFromEntity<youngScholar.entity.User, User>
	{
		private String id;
		private String username;
		private char sex;
		private String mobile;
		private String school;

		@Override
		public User fromEntity(youngScholar.entity.User user)
		{
			id = user.getId();
			username = user.getUsername();
			sex = user.getSex();
			mobile = user.getMobile();
			school = user.getSchool().getSchool();
			return this;
		}
	}
}
