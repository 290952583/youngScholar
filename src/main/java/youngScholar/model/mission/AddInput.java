package youngScholar.model.mission;

import youngScholar.entity.Mission;
import youngScholar.entity.User;
import youngScholar.model.ModelToEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class AddInput implements ModelToEntity<Mission>
{
	@NotNull
	@Length(min = 4, max = 255)
	private String title;

	@NotNull
	@Length(min = 4, max = 5120)
	private String description;

	@NotNull
    @Length(min = 2, max = 255)
	private String address;

	@NotNull
	@Min(0)
	private int price;

	@Override
	public Mission toEntity()
	{
		Mission mission = new Mission();
		mission.setTitle(getTitle());
		mission.setDescription(getDescription());
		mission.setAddress(getAddress());
		mission.setPrice(getPrice());
		mission.setUser(User.getUser());
		return mission;
	}
}
