package youngScholar.model.mission;

import youngScholar.entity.Mission;
import youngScholar.model.ModelUpdateEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class UpdateInput implements ModelUpdateEntity<Mission>
{
	@NotNull
	private String id;

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
	public void update(Mission mission)
	{
		mission.setTitle(title);
		mission.setDescription(description);
		mission.setAddress(address);
		mission.setPrice(price);
	}
}
