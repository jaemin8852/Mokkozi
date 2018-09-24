package mokkozi.com.mokkozi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("api/change/")
    Call<Result> post_information(@Field("original_sentence") String original_sentence);

    @GET("api/change/")
    Call<List<Result>> get_result();

    @GET("api/change/")
    Call<Result> get_res();
}
