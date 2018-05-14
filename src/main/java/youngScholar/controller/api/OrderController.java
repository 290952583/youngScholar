package youngScholar.controller.api;


import com.pingplusplus.model.*;
import youngScholar.Application;
import youngScholar.config.PingxxProperties;
import youngScholar.model.Output;
import youngScholar.model.PageInput;
import youngScholar.model.order.ChargeInput;
import youngScholar.model.order.TransferInput;
import youngScholar.service.OrderService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@RestController
@RequestMapping("api/order")
public class OrderController extends AbstractController
{
    @Autowired
    private OrderService orderService;

    @GetMapping("balance")
    public Output balance()
    {
        return orderService.balance();
    }

    @PostMapping("charge")
    public Output charge(@Valid @RequestBody ChargeInput input)
    {
        return orderService.charge(input);
    }

    @PostMapping("transfer")
    public Output transfer(@Valid @RequestBody TransferInput input)
    {
        return orderService.transfer(input);
    }

    @PostMapping("webHooks")
    public void webHooks(@RequestHeader("x-pingplusplus-signature") String signatureString, @RequestBody String body, HttpServletResponse response) throws Exception
    {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(getPubKey());
        signature.update(body.getBytes("UTF-8"));
        if (!signature.verify(signatureBytes))
        {
            response.setStatus(500);
            return;
        }

        Event event = Webhooks.eventParse(body);
        switch (event.getType())
        {
            case "charge.succeeded":
            {
                Charge charge = (Charge) event.getData().getObject();
                orderService.chargeSucceeded(charge.getOrderNo());
                break;
            }
            case "transfer.succeeded":
            {
                Transfer transfer = (Transfer) event.getData().getObject();
                orderService.transferSucceeded(transfer.getOrderNo());
                break;
            }
            case "transfer.failed":
            {
                Transfer transfer = (Transfer) event.getData().getObject();
                orderService.transferFailed(transfer.getOrderNo());
                break;
            }
        }
        response.setStatus(200);
    }

    private static PublicKey publicKey;

    private static PublicKey getPubKey() throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        if (publicKey == null)
        {
            PingxxProperties pingxxProperties = Application.getBean(PingxxProperties.class);
            byte[] pubKeyBytes = Base64.decodeBase64(pingxxProperties.getPublicKey());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(pubKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(spec);
        }
        return publicKey;
    }

    @GetMapping("list")
    public Output list(@Valid PageInput input)
    {
        return orderService.list(input);
    }
}
