package mokkozi.com.mokkozi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Api {
    //String BASE_URL = "http://5ba91c1f.ngrok.io/";
//    @POST("api/signup/")
//    Call<Information> post_information(@Body Information information);
    @FormUrlEncoded
    @POST("api/signup/")
    Call<Information> post_information(@Field("id") String id, @Field("password") String pw, @Field("email") String email, @Field("gender") String gender);

//    @GET("account/login")
//    Call<List<>>

}
