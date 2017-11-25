package dmitriiserdun.gmail.com.musickiua.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import dmitriiserdun.gmail.com.musickiua.R;

/**
 * Created by dmitro on 12.11.17.
 */

public class Const {
    public final static String BROADCAST_ACTION = "serdun.dmitro.player.servicebroadcastreciver";

    public static final String USER_ID = "user_id";
    public static final String CURRENT_ALBUM_ID = "current_album_id";

    public interface ACTION {
        public static String MAIN_ACTION = "dmitriiserdun.gmail.com.customnotification.action.main";
        public static String INIT_ACTION = "dmitriiserdun.gmail.com.customnotification.action.init";
        public static String PREV_ACTION = "dmitriiserdun.gmail.com.customnotification.action.prev";
        public static String PLAY_ACTION = "dmitriiserdun.gmail.com.customnotification.action.play";
        public static String NEXT_ACTION = "dmitriiserdun.gmail.com.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "dmitriiserdun.gmail.com.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "dmitriiserdun.gmail.com.customnotification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_launcher_foreground, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}
