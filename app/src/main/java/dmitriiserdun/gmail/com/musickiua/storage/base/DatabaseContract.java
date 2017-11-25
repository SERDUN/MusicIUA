package dmitriiserdun.gmail.com.musickiua.storage.base;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dmitro on 23.11.17.
 */

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    //content provider
    public static final String AUTHORITY = "dmitriiserdun.gmail.com.musickiua.storage.provider.sounds";
    public static final String DATABASE_NAME = "tmp_sounds";

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_USER_FIO = "user_fio";
        public String id;


        private static final String SQL_CREATE_USER =
                "CREATE TABLE " + User.TABLE_NAME + " (" +
                        User._ID + " INTEGER PRIMARY KEY," +
                        User.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
                        User.COLUMN_NAME_USER_FIO + TEXT_TYPE + COMMA_SEP + " )";

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + User.TABLE_NAME;

        public static String getSqlCreateUser() {
            return SQL_CREATE_USER;
        }

        public static String getSqlDeleteEntries() {
            return SQL_DELETE_ENTRIES;
        }
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
        public static final String COLUMN_SOUND_ID = "sound_id";


        public static final int MESSAGE_ID_PATH_POSITION = 1;


        public static final String[] DEFAULT_PROJECTION = new String[]{
                Sounds._ID,
                Sounds.COLUMN_NAME_TITLE,
                Sounds.COLUMN_NAME_TIME,
                Sounds.COLUMN_SOUND_ID,
                Sounds.COLUMN_NAME_AUTHOR,
                Sounds.COLUMN_NAME_URL};

//        private static final String SQL_CREATE_SOUNDS =
//                "CREATE TABLE " + Sounds.TABLE_NAME + " (" +
//                        Sounds._ID + " INTEGER PRIMARY KEY," +
//                        Sounds.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
//                        Sounds.COLUMN_NAME_USER_FIO + TEXT_TYPE + COMMA_SEP + " )";


    }
}


