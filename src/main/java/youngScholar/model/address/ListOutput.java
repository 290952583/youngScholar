package youngScholar.model.address;

import youngScholar.entity.Address;
import youngScholar.model.ModelFromEntityList;
import lombok.Data;

@Data
public class ListOutput implements ModelFromEntityList<Address, ListOutput>
{
	private String id;
	private String address;

	@Override
	public ListOutput fromEntity(Address address)
	{
		id = address.getId();
		this.address = address.getAddress();
		return this;
	}
}
