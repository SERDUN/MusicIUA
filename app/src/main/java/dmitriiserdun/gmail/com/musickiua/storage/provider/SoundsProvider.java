package dmitriiserdun.gmail.com.musickiua.storage.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by dmitro on 12.10.17.
 */

public class SoundsProvider extends ContentProvider {
    private static final int DATABASE_VERSION = 1;
    private static HashMap<String, String> soundProjectionMap;


    private static final int SOUND = 1;
    private static final int SOUND_ID = 2;

    private static final UriMatcher uriMatcher;
    private DatabaseHelper dbHelper;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractClass.AUTHORITY, "sound", SOUND);
        uriMatcher.addURI(ContractClass.AUTHORITY, "sound/#", SOUND_ID);


        soundProjectionMap = new HashMap<String, String>();
        for (int i = 0; i < ContractClass.Sounds.DEFAULT_PROJECTION.length; i++) {
            soundProjectionMap.put(
                    ContractClass.Sounds.DEFAULT_PROJECTION[i],
                    ContractClass.Sounds.DEFAULT_PROJECTION[i]);
        }

    }


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case SOUND:
                qb.setTables(ContractClass.Sounds.TABLE_NAME);
                qb.setProjectionMap(soundProjectionMap);
                break;
            case SOUND_ID:
                qb.setTables(ContractClass.Sounds.TABLE_NAME);
                qb.setProjectionMap(soundProjectionMap);
                qb.appendWhere(ContractClass.Sounds._ID + "=" + uri.getPathSegments().get(ContractClass.Sounds.MESSAGE_ID_PATH_POSITION));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case SOUND:
                return ContractClass.Sounds.CONTENT_TYPE;
            case SOUND_ID:
                return ContractClass.Sounds.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues message) {
        if (uriMatcher.match(uri) != SOUND) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;
        if (message != null) {
            values = new ContentValues(message);
        } else {
            values = new ContentValues();
        }
        long rowId = -1;
        Uri rowUri = Uri.EMPTY;

        if (uriMatcher.match(uri) == SOUND) {
            rowId = db.insert(ContractClass.Sounds.TABLE_NAME, null, values);
            if (rowId > 0) {
                rowUri = ContentUris.withAppendedId(ContractClass.Sounds.CONTENT_ID_URI_BASE, rowId);
                getContext().getContentResolver().notifyChange(rowUri, null);
            }
        }


        return rowUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case SOUND:
                count = db.delete(ContractClass.Sounds.TABLE_NAME, selection, selectionArgs);
                break;
            case SOUND_ID:
                finalWhere = ContractClass.Sounds._ID + " = " + uri.getPathSegments().get(ContractClass.Sounds.MESSAGE_ID_PATH_POSITION);
                if (selection != null) {
                    finalWhere = finalWhere + " AND " + selection;
                }
                count = db.delete(ContractClass.Sounds.TABLE_NAME, finalWhere, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (uriMatcher.match(uri)) {
            case SOUND:
                count = db.update(ContractClass.Sounds.TABLE_NAME, values, where, whereArgs);
                break;
            case SOUND_ID:
                id = uri.getPathSegments().get(ContractClass.Sounds.MESSAGE_ID_PATH_POSITION);
                finalWhere = ContractClass.Sounds._ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractClass.Sounds.TABLE_NAME, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        public static final String DATABASE_TABLE_SOUNDS = ContractClass.Sounds.TABLE_NAME;
        public static final String KEY_ROWID = "_id";

        private static final String DATABASE_CREATE_TABLE_MESSAGE =
                "create table " + DATABASE_TABLE_SOUNDS + " ("
                        + KEY_ROWID + " integer primary key autoincrement, "
                        + ContractClass.Sounds.COLUMN_NAME_TITLE + " string , "
                        + ContractClass.Sounds.COLUMN_NAME_TIME + " string , "
                        + ContractClass.Sounds.COLUMN_NAME_AUTHOR + " string , "
                        + ContractClass.Sounds.COLUMN_NAME_URL + " string , "
                        + " UNIQUE ( " + KEY_ROWID + " ) ON CONFLICT IGNORE" + ");";


        private Context ctx;

        DatabaseHelper(Context context) {
            super(context, ContractClass.DATABASE_NAME, null, DATABASE_VERSION);
            ctx = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TABLE_MESSAGE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SOUNDS);
            onCreate(db);
        }
    }

}
