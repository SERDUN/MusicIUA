package dmitriiserdun.gmail.com.musickiua.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dmitro on 11.11.17.
 */

public interface SoundApiService {

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST
    public Observable<Response<ResponseBody>> getToken(@Url String url, @FieldMap HashMap<String, String> field);


    @Headers({"Content-Type: text/html; charset=windows-1251",
            "Connection: keep-alive"})
    @POST
    public Observable<Response<ResponseBody>> login(@Url String url);


    @GET("user/{userId}/")
    public Observable<Response<ResponseBody>> getPlaylistHtml(@Path("userId") Integer userId);


    @GET("user/{userId}/playlist/{playlist_id}")
    public Observable<Response<ResponseBody>> getSoundsHtml(@Path("userId") Integer userId, @Path("playlist_id") String playlist_id);


    @GET("search?_subm=search")
    public Observable<Response<ResponseBody>> searchSound(@Query(value = "words",encoded = true) String words, @Query("subm") int subm, @Query("p") int page);


    @GET
    public Call<ResponseBody> getSoundPlayerFileUrlHtml(@Url String url);

    @GET("get/{sound_id}/{key_for_sound}")
    public Call<ResponseBody> getFileForLoadSound(@Path("sound_id") String soundId, @Path("key_for_sound") String keyForSound);


    @Headers("Content-Type:application/octet-stream")
    @GET
    @Streaming
    public Observable<ResponseBody> getSound(@Url String url);
}
