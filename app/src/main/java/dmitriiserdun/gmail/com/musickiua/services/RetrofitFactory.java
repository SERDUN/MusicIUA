package dmitriiserdun.gmail.com.musickiua.services;

import java.net.CookieManager;
import java.net.CookiePolicy;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.MusicApp;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.ScalarsConverterFactory;

/**
 * Created by dmitro on 11.11.17.
 */

public class RetrofitFactory {
    private static OkHttpClient okHttpClient;
    private static SoundApiService service;

    public static SoundApiService getService() {
        SoundApiService currentService = service;
        if (currentService == null) {
            synchronized (SoundApiService.class) {
                if (currentService == null) {
                    currentService = service = getRetrofitBuilder().create(SoundApiService.class);
                }
            }
        }
        return currentService;
    }


    private static Retrofit getRetrofitBuilder() {
        String url = MusicApp.getInstance().getApplicationContext().getString(R.string.base_url);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClient());


        return builder.build();

    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient client = okHttpClient;
        if (client == null) {
            synchronized (SoundApiService.class) {
                client = okHttpClient;
                if (client == null) {
                    client = okHttpClient = buildOkHttpClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient buildOkHttpClient() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new IInterceptor())
                .build();

        return okHttpClient;
    }

}
