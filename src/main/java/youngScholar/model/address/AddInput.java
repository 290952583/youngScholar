package youngScholar.model.address;

import youngScholar.entity.Address;
import youngScholar.entity.User;
import youngScholar.model.ModelToEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class AddInput implements ModelToEntity<Address>
{
	@NotNull
	@Length(min = 2)
	private String address;

	@Override
	public Address toEntity()
	{
		Address address = new Address();
		address.setAddress(getAddress());
		address.setUser(User.getUser());
		return address;
	}
}
