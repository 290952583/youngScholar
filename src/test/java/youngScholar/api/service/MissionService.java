package youngScholar.api.service;

import youngScholar.entity.Mission;
import youngScholar.model.Output;
import youngScholar.model.mission.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MissionService
{
    @POST("/api/mission/add")
    Call<Output> add(@Body AddInput input);

    @POST("/api/mission/delete")
    Call<Output> delete(@Body DeleteInput input);

    @POST("/api/mission/update")
    Call<Output> update(@Body UpdateInput input);

    @POST("/api/mission/finish")
    Call<Output> finish(@Body FinishInput input);

    @POST("/api/mission/accept")
    Call<Output> accept(@Body AcceptInput input);

    @POST("/api/mission/cancel")
    Call<Output> cancel(@Body CancelInput input);

    @POST("/api/mission/acceptCancel")
    Call<Output> acceptCancel(@Body AcceptCancelInput input);

    @GET("/api/mission/myList")
    Call<Output<List<ListOutput>>> myList(@Query("status") Mission.Status[] statuses, @Query("page") int page);

    @GET("/api/mission/acceptList")
    Call<Output<List<ListOutput>>> acceptList(@Query("status") Mission.Status[] statuses, @Query("page") int page);

    @GET("/api/mission/nearby")
    Call<Output<List<ListOutput>>> nearby(@Query("schoolId") String schoolId, @Query("page") int page);
}
