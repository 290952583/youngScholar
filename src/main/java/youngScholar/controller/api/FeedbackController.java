package youngScholar.controller.api;

import youngScholar.model.Output;
import youngScholar.model.feedback.AddInput;
import youngScholar.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/feedback")
public class FeedbackController extends AbstractController
{
    @Autowired
    FeedbackService feedbackService;

    @PostMapping("add")
    public Output add(@Valid @RequestBody AddInput input)
    {
        return feedbackService.add(input);
    }
}
