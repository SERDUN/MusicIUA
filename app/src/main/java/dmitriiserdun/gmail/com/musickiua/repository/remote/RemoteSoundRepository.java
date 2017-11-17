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

    private final String KEY_USERNAME_PREFIX = "username = \"";
    private final String KEY_USERNAME_POSTFIX = "\";//-->";


    private final String KEY_USER_ID_PREFIX = "<a href=\"http://narod.i.ua/user/";
    private final String KEY_USER_ID_POSTFIX = "/profile/";


    private final String KEY_SCRIPT_PREFIX = ",pk:'";
    private final String KEY_SCRIPT_POSTFIX = "'}],";


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
    public Observable<ResponseBody> getSounds(String url) {
        return RetrofitFactory.getService().getSound(url).flatMap(new Func1<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> call(ResponseBody responseBody) {
                saveToDisk(responseBody);
                return Observable.just(responseBody);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private String findSubscribeText(String html, String firstKey, String secondKey) {
        int startCopyIndex = html.indexOf(firstKey) + firstKey.length();
        int endCopyIndex = html.indexOf(secondKey, startCopyIndex);
        return html.substring(startCopyIndex, endCopyIndex);

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
        ArrayList<Sound> sounds = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements metaElements = doc.select("div[id*=plSongsContainer]");
        Elements metaElements1 = metaElements.select("tr");
        metaElements1.remove(0);
        for (Element element : metaElements1) {
            String soundName = element.select("a").get(1).text();
            String author = element.select("a").get(2).text();
            String time = element.select("td").get(5).text();
            String urlSound = getTrueSoundUrl(element.select("a").first().attr("href"));
            sounds.add(new Sound(soundName, author, time, urlSound));
        }


        return sounds;
    }


    private String getTrueSoundUrl(String path) {

        String pathSound = path.replaceFirst(".*/([^/?]+).*", "$1");

        String url = "http://music.i.ua" + path;
        String finishUrl = null;
        try {
            Response<ResponseBody> response = RetrofitFactory.getService().getSoundPlayerFileUrlHtml(url).execute();
            Document doc = Jsoup.parse(response.body().string());
            Element scriptElements = doc.getElementsByTag("script").get(14);
            String key = getKeyForRequest(scriptElements.data());

            finishUrl = getUrlForLoadAudio(pathSound, key);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return finishUrl;

    }

    private String getUrlForLoadAudio(String path, String key) {
//        u=//mg1.i.ua/g/1f964db620495ce484796c1d04126f15/5a0d5118/music2/2/0/1247702
        final int substringPosition = 4;//--u=//
        try {
            Response<ResponseBody> response = RetrofitFactory.getService().getFileForLoadSound(path, key).execute();
            return response.body().string().substring(substringPosition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getKeyForRequest(String script) {
        return findSubscribeText(script, KEY_SCRIPT_PREFIX, KEY_SCRIPT_POSTFIX);
    }






    public void saveToDisk(ResponseBody body) {
        try {
            File file = new File("storage/emulated/0", "audio.mp3");

            String path = file.getAbsolutePath();
            InputStream is = null;
            OutputStream os = null;

            try {
                Log.d(TAG, "File Size=" + body.contentLength());


                is = body.byteStream();
                os = new FileOutputStream(file);


                byte data[] = new byte[1548576];
                int count;

                int progress = 0;
                while ((count = is.read(data)) != -1) {

                    os.write(data, 0, count);
                    progress += count;

                    Log.d(TAG, "Progress: " + progress + "/" + body.contentLength() + " >>>> " + (float) progress / body.contentLength());
                    os.flush();


                }


                Log.d(TAG, "File saved successfully!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                return;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }
}
