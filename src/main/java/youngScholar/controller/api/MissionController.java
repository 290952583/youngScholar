package youngScholar.controller.api;

import youngScholar.model.Output;
import youngScholar.model.mission.*;
import youngScholar.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/mission")
public class MissionController extends AbstractController
{
    @Autowired
    private MissionService missionService;

    @PostMapping("add")
    public Output add(@Valid @RequestBody AddInput input)
    {
        return missionService.add(input);
    }

    @PostMapping("delete")
    public Output delete(@Valid @RequestBody DeleteInput input)
    {
        return missionService.delete(input);
    }

    @PostMapping("update")
    public Output update(@Valid @RequestBody UpdateInput input)
    {
        return missionService.update(input);
    }

    @PostMapping("finish")
    public Output finish(@Valid @RequestBody FinishInput input)
    {
        return missionService.finish(input);
    }

    @PostMapping("accept")
    public Output accept(@Valid @RequestBody AcceptInput input)
    {
        return missionService.accept(input);
    }

    @PostMapping("cancel")
    public Output cancel(@Valid @RequestBody CancelInput input)
    {
        return missionService.cancel(input);
    }

    @PostMapping("acceptCancel")
    public Output acceptCancel(@Valid @RequestBody AcceptCancelInput input)
    {
        return missionService.acceptCancel(input);
    }

    @GetMapping("myList")
    public Output myList(@Valid StatusesInput input)
    {
        return missionService.myList(input);
    }

    @GetMapping("acceptList")
    public Output acceptList(@Valid StatusesInput input)
    {
        return missionService.acceptList(input);
    }

    @GetMapping("nearby")
    public Output nearby(@Valid NearbyInput input)
    {
        return missionService.nearby(input);
    }
}
