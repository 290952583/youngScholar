package youngScholar.controller.api;

import youngScholar.model.Output;
import youngScholar.model.address.*;
import youngScholar.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/address")
public class AddressController extends AbstractController
{
    @Autowired
    private AddressService addressService;

    @PostMapping("add")
    public Output add(@Valid @RequestBody AddInput input)
    {
        return addressService.add(input);
    }

    @PostMapping("delete")
    public Output delete(@Valid @RequestBody DeleteInput input)
    {
        return addressService.delete(input);
    }

    @PostMapping("update")
    public Output update(@Valid @RequestBody UpdateInput input)
    {
        return addressService.update(input);
    }

    @GetMapping("list")
    public Output list()
    {
        return addressService.list();
    }
}
