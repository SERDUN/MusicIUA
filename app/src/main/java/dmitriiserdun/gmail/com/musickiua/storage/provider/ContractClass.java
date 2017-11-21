package dmitriiserdun.gmail.com.musickiua.storage.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dmitro on 12.10.17.
 */

public class ContractClass {


    public static final String AUTHORITY = "dmitriiserdun.gmail.com.musickiua.storage.provider.sounds";
    public static final String DATABASE_NAME = "sounds";

    private ContractClass() {
    }

    public static final class Sounds implements BaseColumns {

        public static final String TABLE_NAME = "sounds";
        private static final String SCHEME = "content://";
        private static final String PATH_SOUND = "/sound";
        private static final String PATH_SOUND_ID = "/sound/";


        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_SOUND);
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_SOUND_ID);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.sound";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.sound";

        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_URL = "url";


        public static final int MESSAGE_ID_PATH_POSITION = 1;


        public static final String[] DEFAULT_PROJECTION = new String[]{
                Sounds._ID,
                Sounds.COLUMN_NAME_TITLE,
                Sounds.COLUMN_NAME_TIME,
                Sounds.COLUMN_NAME_AUTHOR,
                Sounds.COLUMN_NAME_URL};


    }

}
