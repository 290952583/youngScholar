package youngScholar.api.service;

import youngScholar.model.Output;
import youngScholar.model.order.ChargeInput;
import youngScholar.model.order.TransferInput;
import retrofit2.Call;
import retrofit2.http.*;

public interface OrderService
{
    @GET("/api/order/balance")
    Call<Output> balance();

    @POST("/api/order/charge")
    Call<Output> charge(@Body ChargeInput input);

    @POST("/api/order/transfer")
    Call<Output> transfer(@Body TransferInput input);

    @GET("/api/order/list")
    Call<Output> list();
}
