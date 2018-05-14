package youngScholar.model.mission;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelInput
{
	@NotNull
	private String id;
}
