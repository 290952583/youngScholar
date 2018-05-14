package youngScholar.service;

import youngScholar.entity.Feedback;
import youngScholar.model.Output;
import youngScholar.model.feedback.AddInput;
import youngScholar.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static youngScholar.model.Output.outputOk;
import static youngScholar.model.Output.outputParameterError;

@Service
@Transactional
public class FeedbackService
{
    @Resource
    private FeedbackRepository feedbackRepository;

    public Output add(AddInput input)
    {
        Feedback feedback = feedbackRepository.save(input.toEntity());
        if (feedback == null)
        {
            return outputParameterError();
        }
        return outputOk();
    }
}
