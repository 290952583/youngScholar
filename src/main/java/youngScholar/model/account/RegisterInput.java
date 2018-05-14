package youngScholar.model.account;

import youngScholar.entity.School;
import youngScholar.entity.User;
import youngScholar.model.ModelToEntity;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Validated
public class RegisterInput implements ModelToEntity<User>
{
	@NotNull
	private String username;

	@NotNull
	@Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}$")
	private String mobile;

	@NotNull
	private char sex;

	@NotNull
	private String schoolId;

	private String invitationCode;

	@Override
	public User toEntity()
	{
		User user = new User();
		user.setUsername(username);
		user.setMobile(mobile);
		user.setSex(sex);
		School school = new School();
		school.setId(schoolId);
		user.setSchool(school);
		return user;
	}
}
