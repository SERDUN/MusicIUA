package dmitriiserdun.gmail.com.musickiua.services;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by dmitro on 11.11.17.
 */

public interface SoundApiService {
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Referer:http://touch.i.ua/",
            "Origin:http://touch.i.ua",
            "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "User-Agent:Mozilla/5.0 (Linux; Android 5.1; MI PAD 2 Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/59.0.3071.125 Safari/537.36",
            "Upgrade-Insecure-Requests:1"
    })
    @FormUrlEncoded
    @POST
    public Call<ResponseBody> login(@Url String url, @FieldMap HashMap<String, String> field);
}
