package dmitriiserdun.gmail.com.musickiua.repository.remote;

import android.net.Uri;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.api.RetrofitFactory;
import dmitriiserdun.gmail.com.musickiua.base.MusicApp;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.User;
import dmitriiserdun.gmail.com.musickiua.repository.SoundRepository;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dmitro on 15.11.17.
 */

public class RemoteSoundRepository implements SoundRepository {

    private final String KEY_USERNAME_PREFIX = "username = \"";
    private final String KEY_USERNAME_POSTFIX = "\";//-->";


    private final String KEY_USER_ID_PREFIX = "<a href=\"http://narod.i.ua/user/";
    private final String KEY_USER_ID_POSTFIX = "/profile/";


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

        return RetrofitFactory.getService().getToken(MusicApp.getInstance().getApplicationContext().getString(R.string.login_url), form).flatMap(new Func1<Response<ResponseBody>, Observable<Response<ResponseBody>>>() {
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
//                    String userName= findSubscribeText(html,KEY_USERNAME_PREFIX,KEY_USERNAME_POSTFIX);
                    userId = findSubscribeText(html, KEY_USER_ID_PREFIX, KEY_USER_ID_POSTFIX);

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
        return  RetrofitFactory.getService().getPlaylistHtml(userId).flatMap(new Func1<Response<ResponseBody>, Observable<List<Playlist>>>() {
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

    private String findSubscribeText(String html, String firstKey, String secondKey) {
        int startCopyIndex = html.indexOf(firstKey) + firstKey.length();
        int endCopyIndex = html.indexOf(secondKey, startCopyIndex);
        return html.substring(startCopyIndex, endCopyIndex);

    }


    private List<Playlist> getPlatlistsWithHtml(String html){
        ArrayList<Playlist> playsts=new ArrayList<>();
            Document doc = Jsoup.parse(html);
            Elements metaElements = doc.select("table[id*=plTable]");
            Elements metaElements1 = metaElements.select("tr");
            metaElements1.remove(0);
            metaElements1.remove(metaElements1.size() - 1);

            for (Element element : metaElements1) {
                String sizeSound = element.select("td.align_right").get(0).text();
                String namePlaylist = element.select("a").text();
                String idPlayst = element.select("a").first().attr("href").replaceFirst(".*/([^/?]+).*", "$1");
                playsts.add(new Playlist(namePlaylist,idPlayst,sizeSound));
            }


        return  playsts;
    }
}
