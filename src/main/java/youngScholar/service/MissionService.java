package youngScholar.service;

import youngScholar.entity.*;
import youngScholar.model.Output;
import youngScholar.model.mission.*;
import youngScholar.repository.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

import static youngScholar.model.Output.*;


@Service
@Transactional
public class MissionService
{
    @Resource
    private MissionRepository missionRepository;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private UserRepository userRepository;

    public Output add(AddInput input)
    {
        User user = userRepository.getCurrentUser();
        if (user.getBalance() < input.getPrice())
        {
            return outputInsufficientBalance();
        }
        user.setBalance(user.getBalance() - input.getPrice());
        userRepository.save(user);
        Mission mission = missionRepository.save(input.toEntity());
        if (mission == null)
        {
            return outputParameterError();
        }

        Order order = new Order(user, -input.getPrice(), mission, Order.Type.RELEASE);
        orderRepository.save(order);

        return outputOk();
    }

    public Output delete(DeleteInput input)
    {
        Mission mission = missionRepository.findOne(input.getId());
        if (mission == null)
        {
            return outputParameterError();
        }
        if (!mission.isBelong())
        {
            return outputNotBelong();
        }
        if (mission.getStatus() != Mission.Status.WAIT)
        {
            return outputMissionStatusError();
        }

        User user = mission.getUser();
        user.setBalance(user.getBalance() + mission.getPrice());
        userRepository.save(user);

        orderRepository.deleteByUserAndMission(user, mission);
        missionRepository.delete(mission);

        return outputOk();
    }

    public Output update(UpdateInput input)
    {
        Mission mission = missionRepository.findOne(input.getId());
        if (mission == null)
        {
            return outputParameterError();
        }
        if (!mission.isBelong())
        {
            return outputNotBelong();
        }
        if (mission.getStatus() != Mission.Status.WAIT)
        {
            return outputMissionStatusError();
        }

        User user = mission.getUser();
        user.setBalance(user.getBalance() + input.getPrice() - mission.getPrice());
        if (user.getBalance() < 0)
        {
            return outputInsufficientBalance();
        }

        input.update(mission);
        missionRepository.save(mission);
        userRepository.save(user);
        Order order = orderRepository.findByUserAndMission(user, mission);
        order.setAmount(-mission.getPrice());
        orderRepository.save(order);

        return outputOk();
    }

    public Output finish(FinishInput input)
    {
        Mission mission = missionRepository.findOne(input.getId());
        if (mission == null)
        {
            return outputParameterError();
        }
        if (!mission.isBelong())
        {
            return outputNotBelong();
        }
        if (mission.getStatus() != Mission.Status.PROCESSING)
        {
            return outputMissionStatusError();
        }
        mission.setStatus(Mission.Status.FINISH);
        missionRepository.save(mission);

        User user = mission.getAcceptUser();
        Order order = new Order(user, mission.getPrice(), mission, Order.Type.FINISH);
        orderRepository.save(order);
        user.setBalance(user.getBalance() + mission.getPrice());
        userRepository.save(user);

        return outputOk();
    }

    public Output accept(AcceptInput input)
    {
        User user = userRepository.getCurrentUser();
        if (!user.isAuth())
        {
            return outputNotAuth();
        }
        Mission mission = missionRepository.findOne(input.getId());
        if (mission == null)
        {
            return outputParameterError();
        }
        if (mission.getStatus() != Mission.Status.WAIT)
        {
            return outputMissionStatusError();
        }
        mission.setStatus(Mission.Status.PROCESSING);
        mission.setAcceptUser(user);
        mission.setAcceptTime(new Timestamp(System.currentTimeMillis()));
        missionRepository.save(mission);
        return outputOk();
    }

    public Output cancel(CancelInput input)
    {
        Mission mission = missionRepository.findOne(input.getId());
        if (mission == null)
        {
            return outputParameterError();
        }
        if (!mission.getAcceptUser().getId().equals(User.getUserId()))
        {
            return outputNotBelong();
        }
        if (mission.getStatus() != Mission.Status.PROCESSING)
        {
            return outputMissionStatusError();
        }
        mission.setStatus(Mission.Status.CANCEL);
        missionRepository.save(mission);
        return outputOk();
    }

    public Output acceptCancel(AcceptCancelInput input)
    {
        Mission mission = missionRepository.findOne(input.getId());
        if (mission == null)
        {
            return outputParameterError();
        }
        if (!mission.isBelong())
        {
            return outputNotBelong();
        }
        if (mission.getStatus() != Mission.Status.CANCEL)
        {
            return outputMissionStatusError();
        }
        mission.setStatus(Mission.Status.WAIT);
        mission.setAcceptUser(null);
        mission.setAcceptTime(null);
        missionRepository.save(mission);
        return outputOk();
    }

    public Output<List<ListOutput>> myList(StatusesInput input)
    {
        List<Mission> missions = missionRepository.findAllByUserAndStatusIn(User.getUser(), input.getStatuses(), input.getPageableSortByTime());
        List<ListOutput> outputs = new ListOutput().fromEntityList(missions);
        return output(outputs);
    }

    public Output<List<ListOutput>> acceptList(StatusesInput input)
    {
        List<Mission> missions = missionRepository.findAllByAcceptUserAndStatusIn(User.getUser(), input.getStatuses(), input.getPageableSortByTime());
        List<ListOutput> outputs = new ListOutput().fromEntityList(missions);
        return output(outputs);
    }

    public Output<List<ListOutput>> nearby(NearbyInput input)
    {
        School school = new School();
        school.setId(input.getSchoolId());
        Mission.Status[] statuses = {Mission.Status.WAIT, Mission.Status.PROCESSING};
        List<Mission> missions = missionRepository.findAllByUserSchoolAndStatusIn(school, statuses, input.getPageableSortByTime());
        List<ListOutput> outputs = new ListOutput().fromEntityList(missions);
        return output(outputs);
    }
}
