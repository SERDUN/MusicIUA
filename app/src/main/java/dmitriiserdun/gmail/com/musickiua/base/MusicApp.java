package dmitriiserdun.gmail.com.musickiua.base;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.orhanobut.hawk.Hawk;


/**
 * Created by dmitro on 31.10.17.
 */

public class MusicApp extends Application {
    private static MusicApp instance;
    private BaseActivity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Hawk.init(this).build();

        instance = this;
    }

    public static MusicApp getInstance() {
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
