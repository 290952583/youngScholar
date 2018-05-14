package youngScholar.model.order;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class ChargeInput
{
	@NotNull
	@Min(1)
	private int amount;
	@NotNull
	private String channel;
}
