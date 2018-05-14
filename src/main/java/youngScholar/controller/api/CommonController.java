package youngScholar.controller.api;

import youngScholar.model.Output;
import youngScholar.model.common.SendSmsInput;
import youngScholar.service.SchoolService;
import youngScholar.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static youngScholar.model.Output.outputOk;

@RestController
@RequestMapping("api/common")
public class CommonController extends AbstractController
{
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SmsService smsService;

    @PostMapping("sendSms")
    public Output sendSms(@Valid @RequestBody SendSmsInput input)
    {
        return smsService.sendSms(input);
    }

    @GetMapping("school")
    public Output school()
    {
        return schoolService.list();
    }

    @GetMapping("testLogin")
    public Output testLogin()
    {
        autoLogin("18201265103");
        return outputOk();
    }
}
