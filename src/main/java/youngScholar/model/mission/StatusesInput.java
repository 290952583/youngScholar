package youngScholar.model.mission;

import youngScholar.entity.Mission;
import youngScholar.model.PageInput;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class StatusesInput extends PageInput
{
    @NotNull
    private Mission.Status[] statuses;
}
