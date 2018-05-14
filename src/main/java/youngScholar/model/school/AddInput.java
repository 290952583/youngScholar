package youngScholar.model.school;

import youngScholar.entity.School;
import youngScholar.model.ModelToEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class AddInput implements ModelToEntity<School>
{
	@Length(min = 4, max = 255)
	private String school;

	@Override
	public School toEntity()
	{
		School school = new School();
		school.setSchool(this.school);
		return school;
	}
}
