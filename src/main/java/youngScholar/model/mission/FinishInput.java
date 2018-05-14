package youngScholar.model.mission;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class FinishInput
{
	@NotNull
	private String id;
}
