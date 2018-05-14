package youngScholar.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import youngScholar.api.service.*;
import youngScholar.entity.Mission;
import youngScholar.model.Output;
import youngScholar.model.address.AddInput;
import youngScholar.model.address.DeleteInput;
import youngScholar.model.address.ListOutput;
import youngScholar.model.address.UpdateInput;
import youngScholar.model.common.SendSmsInput;
import youngScholar.model.mission.*;
import youngScholar.model.order.ChargeInput;
import youngScholar.model.order.TransferInput;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ApiTests extends Assert
{
    private final String mobile = "17864154930";

    private final Retrofit retrofit;

    public ApiTests() throws Exception
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .cookieJar(new CookieJar()
                {
                    private HashSet<Cookie> cookies = new HashSet<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
                    {
                        this.cookies.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url)
                    {
                        return new LinkedList<>(cookies);
                    }
                }).build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build();

        CommonService commonService = retrofit.create(CommonService.class);

        Response<Output> response = commonService.testLogin().execute();
        Output output = response.body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
    }

    @Test
    public void testCommon() throws Exception
    {
        CommonService service = retrofit.create(CommonService.class);

        Output output = service.school().execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        SendSmsInput input = new SendSmsInput();
        input.setMobile(mobile);
        output = service.sendSms(input).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
    }

    @Test
    public void testAddress() throws Exception
    {
        AddressService service = retrofit.create(AddressService.class);

        Output<List<ListOutput>> list = service.list().execute().body();
        if (list.getData().size() == 5)
        {
            DeleteInput deleteInput = new DeleteInput();
            deleteInput.setId(list.getData().get(0).getId());
            Output output = service.delete(deleteInput).execute().body();
            assertEquals(output.getCodeInfo(), Output.Code.OK);
            list = service.list().execute().body();
        }

        AddInput addInput = new AddInput();
        addInput.setAddress("地址");
        Output output = service.add(addInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        UpdateInput updateInput = new UpdateInput();
        updateInput.setId(list.getData().get(0).getId());
        updateInput.setAddress("修改地址");
        output = service.update(updateInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
    }

    @Test
    public void testMission() throws Exception
    {
        MissionService service = retrofit.create(MissionService.class);

        // 添加
        youngScholar.model.mission.AddInput addInput = new youngScholar.model.mission.AddInput();
        addInput.setTitle("测试标题");
        addInput.setDescription("测试内容");
        addInput.setAddress("地址");
        addInput.setPrice(10000);
        Output output = service.add(addInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
        output = service.add(addInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        // 查询
        Output<List<youngScholar.model.mission.ListOutput>> listOutput =
                service.acceptList(new Mission.Status[]{Mission.Status.WAIT}, 0).execute().body();
        assertEquals(listOutput.getCodeInfo(), Output.Code.OK);
        listOutput = service.nearby("0", 0).execute().body();
        assertEquals(listOutput.getCodeInfo(), Output.Code.OK);
        listOutput = service.myList(new Mission.Status[]{Mission.Status.WAIT}, 0).execute().body();
        assertEquals(listOutput.getCodeInfo(), Output.Code.OK);

        // 删除
        youngScholar.model.mission.DeleteInput deleteInput = new youngScholar.model.mission.DeleteInput();
        deleteInput.setId(listOutput.getData().get(0).getId());
        output = service.delete(deleteInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);


        listOutput = service.myList(new Mission.Status[]{Mission.Status.WAIT}, 0).execute().body();
        assertEquals(listOutput.getCodeInfo(), Output.Code.OK);
        youngScholar.model.mission.ListOutput mission = listOutput.getData().get(0);

        // 修改
        youngScholar.model.mission.UpdateInput updateInput = new youngScholar.model.mission.UpdateInput();
        updateInput.setAddress("地址二");
        updateInput.setDescription(mission.getDescription());
        updateInput.setId(mission.getId());
        updateInput.setPrice(mission.getPrice() + 100);
        updateInput.setTitle(mission.getTitle());
        output = service.update(updateInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        // 接收任务
        AcceptInput acceptInput = new AcceptInput();
        acceptInput.setId(mission.getId());
        output = service.accept(acceptInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        // 取消任务
        CancelInput cancelInput = new CancelInput();
        cancelInput.setId(mission.getId());
        output = service.cancel(cancelInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        // 确认取消
        AcceptCancelInput acceptCancelInput = new AcceptCancelInput();
        acceptCancelInput.setId(mission.getId());
        output = service.acceptCancel(acceptCancelInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);


        acceptInput = new AcceptInput();
        acceptInput.setId(mission.getId());
        output = service.accept(acceptInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);

        // 完成任务
        FinishInput finishInput = new FinishInput();
        finishInput.setId(mission.getId());
        output = service.finish(finishInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
    }

    @Test
    public void testOrder() throws Exception
    {
        OrderService service = retrofit.create(OrderService.class);

        // 查询余额
        Output output = service.balance().execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
        System.out.println(output.getData());

        // 充值
        ChargeInput chargeInput = new ChargeInput();
        chargeInput.setAmount(10000);
        chargeInput.setChannel("alipay");
        output = service.charge(chargeInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
        System.out.println(output.getData());

        // 提现
        TransferInput transferInput = new TransferInput();
        transferInput.setName("苏庆发");
        transferInput.setChannel("alipay");
        transferInput.setRecipient(mobile);
        transferInput.setAmount(1000);
        output = service.transfer(transferInput).execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.ParameterError);

        // 账单
        output = service.list().execute().body();
        assertEquals(output.getCodeInfo(), Output.Code.OK);
        System.out.println(output.getData());
    }
}
