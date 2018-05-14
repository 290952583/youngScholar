package youngScholar.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import youngScholar.config.SmsProperties;
import youngScholar.model.Output;
import youngScholar.model.common.SendSmsInput;
import youngScholar.util.GenerateRandomSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static youngScholar.model.Output.outputOk;
import static youngScholar.model.Output.outputParameterError;

@Service
public class SmsService
{
    @Resource
    private SmsProperties smsProperties;
    @Autowired
    private HttpSession session;
    @Autowired
    private GenerateRandomSequence randomSequence;

    private static final String MOBILE = "mobile";
    private static final String SMSCODE = "smsCode";
    private static final String SENDTIME = "sendTime";
    private static final String VERIFICATIONMOBILE = "verificationMobile";

    public Output sendSms(SendSmsInput input)
    {
        String code = send(input.getMobile());
        if (code == null)
        {
            return outputParameterError();
        }
        setSmsSession(input.getMobile(), code);
        return outputOk();
    }

    private void setSmsSession(String mobile, String code)
    {
        session.setAttribute(MOBILE, mobile);
        session.setAttribute(SMSCODE, code);
        session.setAttribute(SENDTIME, LocalDateTime.now());
    }

    public boolean verificationSmsSession(String mobile, String code)
    {
        if (mobile == null || code == null || !mobile.equals(session.getAttribute(MOBILE)))
        {
            return false;
        }

        Object sendTime = session.getAttribute(SENDTIME);
        // 验证码有效时间 300s
        LocalDateTime effectiveTime = LocalDateTime.now().minusSeconds(300);
        if (sendTime == null || effectiveTime.isAfter((LocalDateTime) sendTime))
        {
            return false;
        }

        Object smsCode = session.getAttribute(SMSCODE);
        if (smsCode == null || !smsCode.equals(code))
        {
            return false;
        }

        session.setAttribute(VERIFICATIONMOBILE, session.getAttribute(MOBILE));
        session.removeAttribute(SENDTIME);
        session.removeAttribute(SMSCODE);
        session.removeAttribute(MOBILE);

        return true;
    }

    public String getVerificationMobile()
    {
        Object mobile = session.getAttribute(VERIFICATIONMOBILE);
        if (mobile == null)
        {
            return null;
        }
        return (String) mobile;
    }

    private static IAcsClient acsClient;

    private static IAcsClient getClient(SmsProperties smsProperties)
    {
        if (acsClient == null)
        {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            final String product = "Dysmsapi";
            final String domain = "dysmsapi.aliyuncs.com";

            final String accessKeyId = smsProperties.getAccessKeyId();
            final String accessKeySecret = smsProperties.getAccessKeySecret();
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            try
            {
                DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            }
            catch (ClientException e)
            {
                e.printStackTrace();
            }
            acsClient = new DefaultAcsClient(profile);
        }
        return acsClient;
    }

    private String send(String mobile)
    {
        String code = randomSequence.getRandomNumber();

        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(mobile);
        request.setSignName(smsProperties.getSignName());
        request.setTemplateCode(smsProperties.getTemplateCode());
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        try
        {
            SendSmsResponse sendSmsResponse = getClient(smsProperties).getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK"))
            {
                return code;
            }
        }
        catch (ClientException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
