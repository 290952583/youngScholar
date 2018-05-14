package youngScholar.service;

import com.taobao.api.*;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import youngScholar.config.TaobaoProperties;
import youngScholar.entity.Image;
import youngScholar.entity.User;
import youngScholar.model.Output;
import youngScholar.model.account.*;
import youngScholar.repository.ImageRepository;
import youngScholar.repository.UserRepository;
import youngScholar.util.GenerateRandomSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;

import static youngScholar.model.Output.*;

@Service
@Transactional
public class AccountService
{
    @Resource
    private UserRepository userRepository;
    @Resource
    private ImageRepository imageRepository;
    @Autowired
    private GenerateRandomSequence randomSequence;
    @Resource
    private TaobaoProperties taobaoProperties;

    public User findUserByMobile(String mobile)
    {
        return userRepository.findByMobile(mobile);
    }

    public Output<UserInfoOutput> register(RegisterInput input)
    {
        User user = userRepository.findByMobile(input.getMobile());
        if (user != null)
        {
            return outputUserExists();
        }

        user = userRepository.save(input.toEntity());

        //阿里云旺
        TaobaoClient client = new DefaultTaobaoClient(taobaoProperties.getUrl(), taobaoProperties.getAppKey(), taobaoProperties.getSecret());

        OpenimUsersAddRequest request = new OpenimUsersAddRequest();
        Userinfos userinfos = new Userinfos();
        userinfos.setNick(user.getUsername());
        userinfos.setIconUrl(taobaoProperties.getIconUrl() + user.getMobile());
        userinfos.setUserid(user.getMobile());
        userinfos.setPassword(taobaoProperties.getPassword());
        request.setUserinfos(Collections.singletonList(userinfos));

        try
        {
            client.execute(request);
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }

        // 修改发送邀请码者数据
        if (input.getInvitationCode() != null)
        {
            userRepository.addInvitationCount(input.getInvitationCode());
        }

        return output(new UserInfoOutput().fromEntity(userRepository.findOne(user.getId())));
    }

    public Output auth(AuthInput input) throws IOException
    {
        User user = userRepository.getCurrentUser();
        user.setName(input.getName());
        user.setIdCard(input.getIdCard());
        // TODO 测试阶段直接通过
        // user.setAuthStatus(User.AuthStatus.WAIT);
        user.setAuthStatus(User.AuthStatus.PASS);
        userRepository.save(user);

        Image image = new Image();

        image.setName("Auth/" + user.getId() + "/1");
        image.setData(input.getFile1()
                .getBytes());
        imageRepository.save(image);

        image.setName("Auth/" + user.getId() + "/2");
        image.setData(input.getFile2()
                .getBytes());
        imageRepository.save(image);

        return outputOk();
    }

    public Output<AuthOutput> getAuthStatus(String id)
    {
        User user = userRepository.findOne(id);
        if (user == null)
        {
            return outputParameterError();
        }
        AuthOutput output = new AuthOutput().fromEntity(user);
        return output(output);
    }

    public Output<UserInfoOutput> getUserInfo(String id)
    {
        User user = userRepository.getOne(id);
        if (user == null)
        {
            return outputParameterError();
        }

        return output(new UserInfoOutput().fromEntity(user));
    }

    public Output<InvitationCodeOutput> getInvitationCode()
    {
        User user = userRepository.getCurrentUser();
        if (user.getInvitationCode() == null)
        {
            user.setInvitationCode(randomSequence.getRandomUppercaseNumber());
            userRepository.save(user);
        }
        InvitationCodeOutput output = new InvitationCodeOutput().fromEntity(user);
        return output(output);
    }

    public byte[] getUserAvatar(String UserId)
    {
        Image image = imageRepository.findOne("UserAvatar/" + UserId);
        if (image == null)
        {
            image = imageRepository.findOne("UserAvatar");
        }
        return image.getData();
    }

    public Output setUserAvatar(byte[] data)
    {
        Image image = new Image();
        image.setName("UserAvatar/" + User.getUserId());
        image.setData(data);
        imageRepository.save(image);

        TaobaoClient client = new DefaultTaobaoClient(taobaoProperties.getUrl(), taobaoProperties.getAppKey(), taobaoProperties.getSecret());

        OpenimUsersUpdateRequest request = new OpenimUsersUpdateRequest();
        Userinfos userinfos = new Userinfos();
        userinfos.setIconUrl(taobaoProperties.getIconUrl() + userRepository.getCurrentUser()
                .getMobile());
        request.setUserinfos(Collections.singletonList(userinfos));

        try
        {
            client.execute(request);
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }

        return outputOk();
    }
}
