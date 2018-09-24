package mokkozi.com.mokkozi;

import android.app.Application;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {
    public final static String TAG = "adsf";
    private static ApplicationController instance;
    public static ApplicationController getInstance(){return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
    }

    private Api networkService;
    public Api getNetworkService() {return networkService;}

    private String baseUrl;

    public void buildNetworkService(String ip){
        synchronized (ApplicationController.class){
            if (networkService == null){
                baseUrl = String.format("http://%s/", ip);
                Log.i(TAG, baseUrl);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                networkService = retrofit.create(Api.class);
            }
        }
    }
}
