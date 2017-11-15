package dmitriiserdun.gmail.com.musickiua.api;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.MusicApp;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MusicApp.getInstance()));


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new IInterceptor())
                .cookieJar(cookieJar)
                .followRedirects(false).followRedirects(false)
                .build();

        return okHttpClient;
    }

}
