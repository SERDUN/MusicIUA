package dmitriiserdun.gmail.com.musickiua.repository.remote;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.api.RetrofitFactory;
import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.FoundSounds;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.repository.SoundRepository;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by dmitro on 15.11.17.
 */

public class RemoteSoundRepository implements SoundRepository {


    private static RemoteSoundRepository INSTANCE;

    public static RemoteSoundRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteSoundRepository();
        }
        return INSTANCE;
    }

    private RemoteSoundRepository() {
    }

    @Override
    public Observable<Integer> login(String login, String pass) {
        final HashMap<String, String> form = new HashMap<>();
        form.put("_subm", "lform");
        form.put("login", login);
        form.put("pass", pass);
        form.put("domn", "i.ua");

        return RetrofitFactory.getService().getToken(App.getInstance().getApplicationContext().getString(R.string.login_url), form).flatMap(new Func1<Response<ResponseBody>, Observable<Response<ResponseBody>>>() {
            @Override
            public Observable<Response<ResponseBody>> call(Response<ResponseBody> responseBodyResponse) {
                String loginUrl = responseBodyResponse.headers().get("Location");
                return RetrofitFactory.getService().login(loginUrl);
            }
        }).flatMap(new Func1<Response<ResponseBody>, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Response<ResponseBody> responseBodyResponse) {
                String userId = null;
                try {
                    String html = responseBodyResponse.body().string();
                    userId = HTMLHelperConverter.getUserId(html);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Observable.just(Integer.valueOf(userId));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<List<Playlist>> getPlaylists(Integer userId) {
        return RetrofitFactory.getService().getPlaylistHtml(userId).flatMap(new Func1<Response<ResponseBody>, Observable<List<Playlist>>>() {
            @Override
            public Observable<List<Playlist>> call(Response<ResponseBody> responseBodyResponse) {
                try {
                    return Observable.just(getPlatlistsWithHtml(responseBodyResponse.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<List<Sound>> getSounds(Integer userId, String playlist_id) {

        return RetrofitFactory.getService().getSoundsHtml(userId, playlist_id).flatMap(new Func1<Response<ResponseBody>, Observable<List<Sound>>>() {
            @Override
            public Observable<List<Sound>> call(Response<ResponseBody> responseBodyResponse) {

                try {
                    return Observable.just(getSoundsWithHtml(responseBodyResponse.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FoundSounds> searchSounds(String words, int page) {
        return RetrofitFactory.getService().searchSound(words, 1, page).flatMap(new Func1<Response<ResponseBody>, Observable<FoundSounds>>() {
            @Override
            public Observable<FoundSounds> call(Response<ResponseBody> response) {
                try {
                    return Observable.just(HTMLHelperConverter.getFoundSounds(response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    @Override
    public Observable<ResponseBody> getSounds(String url) {
        return RetrofitFactory.getService().getSound(url).flatMap(new Func1<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> call(ResponseBody responseBody) {
                return Observable.just(responseBody);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private List<Playlist> getPlatlistsWithHtml(String html) {
        ArrayList<Playlist> playsts = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements metaElements = doc.select("div.search_result.clear");
        Elements metaElements1 = metaElements.select("tr");
        metaElements1.remove(0);
        //    metaElements1.remove(metaElements1.size() - 1);

        for (Element element : metaElements1) {
            String sizeSound = element.select("td.align_right").get(0).text();
            String namePlaylist = element.select("a").text();
            String idPlayst = element.select("a").first().attr("href").replaceFirst(".*/([^/?]+).*", "$1");
            playsts.add(new Playlist(namePlaylist, idPlayst, sizeSound));
        }


        return playsts;
    }


    private List<Sound> getSoundsWithHtml(String html) {
        Document doc = Jsoup.parse(html);
        Elements metaElements = doc.select("div[id*=plSongsContainer]");
        Elements soundContent = metaElements.select("tr");
        soundContent.remove(0);
        return HTMLHelperConverter.getSounds(soundContent);
    }


}
