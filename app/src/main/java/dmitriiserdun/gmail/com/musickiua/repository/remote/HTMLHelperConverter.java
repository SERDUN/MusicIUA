package dmitriiserdun.gmail.com.musickiua.repository.remote;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.api.RetrofitFactory;
import dmitriiserdun.gmail.com.musickiua.model.FoundSounds;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by dmitro on 20.11.17.
 */

public class HTMLHelperConverter {

    private static final String KEY_USERNAME_PREFIX = "username = \"";
    private static final String KEY_USERNAME_POSTFIX = "\";//-->";

    private static final String KEY_SCRIPT_PREFIX = ",pk:'";
    private static final String KEY_SCRIPT_POSTFIX = "'}],";

    private static final String KEY_USER_ID_PREFIX = "<a href=\"http://narod.i.ua/user/";
    private static final String KEY_USER_ID_POSTFIX = "/profile/";


    private static final String KEY_SEARCHED_SOUND_CONTENT_PREFIX = "<div class=\"search_result clear\">";
    private static final String KEY_SEARCHED_SOUND_CONTENT_POSTFIX = "<div class=\"pager clear\"><dl><dt>";

    private static final String KEY_SEARCHED_POSITION_CONTENT_PREFIX = "<div class=\"pager clear\"><dl><dt>";
    private static final String KEY_SEARCHED_POSITION_CONTENT_POSTFIX = "</dd></dl></div>";

    private static final int count_element_control = 2;

    public static ArrayList<Sound> getSounds(Elements elements) {
        ArrayList<Sound> sounds = new ArrayList<>();
        for (Element element : elements) {
            String soundName = element.select("a").get(1).text();
            String author = element.select("a").get(2).text();
            String time = element.select("td").get(5).text();
            String urlSound = findRealSoundUrlInHtml(element.select("a").first().attr("href"));
            sounds.add(new Sound(soundName, author, time, urlSound));
        }
        return sounds;
    }


    public static String getUserId(String html) {
        return findSubscribeText(html, KEY_USER_ID_PREFIX, KEY_USER_ID_POSTFIX);

    }


    public static FoundSounds getFoundSounds(String html) {
        int startCpContent = html.indexOf(KEY_SEARCHED_SOUND_CONTENT_PREFIX);
        int endCpContent = html.indexOf(KEY_SEARCHED_SOUND_CONTENT_POSTFIX);

        int startCpPosition = html.indexOf(KEY_SEARCHED_POSITION_CONTENT_PREFIX);
        int endCpPosition = html.indexOf(KEY_SEARCHED_POSITION_CONTENT_POSTFIX);

        String contentSoundHtml = html.substring(startCpContent, endCpContent);
        String positionSoundHtml = html.substring(startCpPosition, endCpPosition);

        Document doc = Jsoup.parse(contentSoundHtml);
        Document positionDoc = Jsoup.parse(positionSoundHtml);

        Elements soundsContent = doc.select("tr");
        soundsContent.remove(0);

        ArrayList<Sound> sounds = HTMLHelperConverter.getSounds(soundsContent);
        FoundSounds foundSounds = new FoundSounds(sounds);


        Elements positionElements = positionDoc.select("span.current");
        Elements maxPosition = positionDoc.select("dd");
        foundSounds.setCurrentPage(positionElements.last().text());
        foundSounds.setMaxPage(maxPosition.get(maxPosition.size() - count_element_control - 1).text());

        return foundSounds;
    }

    private static String findRealSoundUrlInHtml(String path) {

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


    private static String getUrlForLoadAudio(String path, String key) {
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

    private static String getKeyForRequest(String script) {
        return findSubscribeText(script, KEY_SCRIPT_PREFIX, KEY_SCRIPT_POSTFIX);
    }

    private static String findSubscribeText(String html, String firstKey, String secondKey) {
        int startCopyIndex = html.indexOf(firstKey) + firstKey.length();
        int endCopyIndex = html.indexOf(secondKey, startCopyIndex);
        return html.substring(startCopyIndex, endCopyIndex);

    }
}
