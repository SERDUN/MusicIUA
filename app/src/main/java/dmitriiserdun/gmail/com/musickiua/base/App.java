package dmitriiserdun.gmail.com.musickiua.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.orhanobut.hawk.Hawk;

import java.io.File;


/**
 * Created by dmitro on 31.10.17.
 */

public class App extends Application {
    private static App instance;
    private BaseActivity currentActivity;
    private HttpProxyCacheServer proxy;
    private static String tmpName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Hawk.init(this).build();

        instance = this;
    }

    public static HttpProxyCacheServer getProxy(Context context, String name) {
        App app = (App) context.getApplicationContext();
        tmpName = name;
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        File file = new File("/storage/sdcard1", "iua");
        return new HttpProxyCacheServer.Builder(this)
                .fileNameGenerator(new FileNameGenerator() {
                    @Override
                    public String generate(String url) {
                        //   Uri uri = Uri.parse(url);
                        // String videoId = uri.getQueryParameter("name");
                        return App.tmpName + ".mp3";
                    }
                }).cacheDirectory(file).build();
    }

    public static App getInstance() {
        if (instance == null) {
            throw new RuntimeException("Application initialization error!");
        }
        return instance;
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }
}
