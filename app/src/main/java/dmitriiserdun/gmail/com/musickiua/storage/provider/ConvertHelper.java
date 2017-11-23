package dmitriiserdun.gmail.com.musickiua.storage.provider;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.storage.base.DatabaseContract;

/**
 * Created by dmitro on 21.11.17.
 */

public class ConvertHelper {
    public static ContentValues createContentValue(Sound sound) {
        ContentValues value = new ContentValues();
        value.put(DatabaseContract.Sounds.COLUMN_NAME_TITLE, sound.getName());
        value.put(DatabaseContract.Sounds.COLUMN_NAME_AUTHOR, sound.getAuthor());
        value.put(DatabaseContract.Sounds.COLUMN_NAME_TIME, sound.getTime());
        value.put(DatabaseContract.Sounds.COLUMN_NAME_URL, sound.getUrl());
        value.put(DatabaseContract.Sounds.COLUMN_SOUND_ID, sound.getSoundId());
        return value;
    }

    public static ContentValues[] createContentValues(List<Sound> sounds) {
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        for (Sound sound : sounds) {
            contentValues.add(createContentValue(sound));
        }
        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }


    public static Sound createSound(Cursor cursor) {
        return parseCursor(cursor).get(0);
    }

    public static ArrayList<Sound> createSounds(Cursor cursor) {
        return parseCursor(cursor);
    }


    private static ArrayList<Sound> parseCursor(Cursor cursor) {
        ArrayList<Sound> sounds = new ArrayList<>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    String soundName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Sounds.COLUMN_NAME_TITLE));
                    String author = cursor.getString(cursor.getColumnIndex(DatabaseContract.Sounds.COLUMN_NAME_AUTHOR));
                    String time = cursor.getString(cursor.getColumnIndex(DatabaseContract.Sounds.COLUMN_NAME_TIME));
                    String url = cursor.getString(cursor.getColumnIndex(DatabaseContract.Sounds.COLUMN_NAME_URL));
                    String soundId = cursor.getString(cursor.getColumnIndex(DatabaseContract.Sounds.COLUMN_SOUND_ID));
                    sounds.add(new Sound(soundName, author, soundId, time, url));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return sounds;

    }

}

