package dmitriiserdun.gmail.com.musickiua.base.player;

import android.net.Uri;

import com.danikula.videocache.file.FileNameGenerator;

/**
 * Created by dmitro on 18.11.17.
 */

public class MyFileGenerator implements FileNameGenerator {
    public String generate(String url) {
        Uri uri = Uri.parse(url);
        String videoId = uri.getQueryParameter("name");
        return videoId + ".mp3";
    }
}
