package youngScholar;

import youngScholar.entity.School;
import youngScholar.entity.User;
import youngScholar.model.account.RegisterInput;
import youngScholar.model.school.AddInput;
import youngScholar.repository.SchoolRepository;
import youngScholar.service.AccountService;
import youngScholar.service.SchoolService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests extends Assert
{
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testAccount()
	{
		AccountService service = Application.getBean(AccountService.class);
		User user = service.findUserByMobile("18006352857");
		if (user == null)
		{
			SchoolRepository schoolRepository = Application.getBean(SchoolRepository.class);
			List<School> list = schoolRepository.findAll();
			if (list.size() == 0)
			{
				SchoolService schoolService = Application.getBean(SchoolService.class);
				AddInput input = new AddInput();
				input.setSchool("山东大学");
				schoolService.add(input);
				list = schoolRepository.findAll();
			}
			RegisterInput input = new RegisterInput();
			input.setMobile("18006352857");
			input.setUsername("赵衍琛");
			input.setSex('男');
			input.setSchoolId(list.get(0).getId());
			service.register(input);
			user = service.findUserByMobile("18006352857");
		}
		logger.warn(user.toString());
	}
}
