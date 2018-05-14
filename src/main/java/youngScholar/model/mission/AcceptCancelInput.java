package youngScholar.model.mission;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AcceptCancelInput
{
	@NotNull
	private String id;
}
