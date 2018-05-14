package youngScholar.service;

import youngScholar.entity.School;
import youngScholar.model.Output;
import youngScholar.model.school.AddInput;
import youngScholar.model.school.ListOutput;
import youngScholar.repository.SchoolRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static youngScholar.model.Output.output;
import static youngScholar.model.Output.outputOk;

@Service
@Transactional
public class SchoolService
{
    @Resource
    private SchoolRepository schoolRepository;

    public Output add(AddInput input)
    {
        schoolRepository.save(input.toEntity());
        return outputOk();
    }

    public Output<List<ListOutput>> list()
    {
        List<School> schools = schoolRepository.findAll();
        List<ListOutput> outputs = new ListOutput().fromEntityList(schools);
        return output(outputs);
    }
}
