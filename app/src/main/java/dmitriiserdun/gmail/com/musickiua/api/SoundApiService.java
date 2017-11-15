package dmitriiserdun.gmail.com.musickiua.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dmitro on 11.11.17.
 */

public interface SoundApiService {
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST
    public Call<ResponseBody> test(@Url String url, @FieldMap HashMap<String, String> field);


    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST
    public Observable<Response<ResponseBody>> getToken(@Url String url, @FieldMap HashMap<String, String> field);


    @Headers({"Content-Type: text/html; charset=windows-1251",
            "Connection: keep-alive"})

    @POST
    public Observable<Response<ResponseBody>> login(@Url String url);
}
