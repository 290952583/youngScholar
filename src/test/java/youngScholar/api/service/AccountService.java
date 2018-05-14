package youngScholar.api.service;

import youngScholar.model.Output;
import youngScholar.model.account.*;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AccountService
{
    @FormUrlEncoded
    @POST("/api/account/login")
    Call<Output> login(@Field("username") String username, @Field("password") String password);

    @POST("/api/account/logout")
    Call<Output> logout();

    @POST("/api/account/register")
    Call<Output> register(@Body RegisterInput input);

    @Multipart
    @POST("/api/account/auth")
    Call<Output> auth(@Field("name") String username, @Field("idCard") String name,
                      @Part("file1") MultipartBody.Part file1,
                      @Part("file2") MultipartBody.Part file2);

    @GET("/api/account/getUserInfo/{id}")
    Call<Output<UserInfoOutput>> getUserInfo(@Path("id") String id);

    @GET("/api/account/getInvitationCode")
    Call<Output<InvitationCodeOutput>> getInvitationCode();

    @GET("/api/account/getUserAvatar/{id}")
    Call<ResponseBody> getUserAvatar(@Path("id") String id);

    @Multipart
    @POST("/api/account/setUserAvatar")
    Call<Output> setUserAvatar(@Part("file") MultipartBody.Part file);
}