package youngScholar.model.address;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class DeleteInput
{
	@NotNull
	private String id;
}
