package dmitriiserdun.gmail.com.musickiua.storage.provider;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.model.Sound;

/**
 * Created by dmitro on 21.11.17.
 */

public class ConvertHelper {
    public static ContentValues createContentValues(Sound sound) {
        ContentValues value = new ContentValues();
        value.put(ContractClass.Sounds.COLUMN_NAME_TITLE, sound.getName());
        value.put(ContractClass.Sounds.COLUMN_NAME_AUTHOR, sound.getAuthor());
        value.put(ContractClass.Sounds.COLUMN_NAME_TIME, sound.getTime());
        value.put(ContractClass.Sounds.COLUMN_NAME_URL, sound.getUrl());
        return value;
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
                    String soundName = cursor.getString(cursor.getColumnIndex(ContractClass.Sounds.COLUMN_NAME_TIME));
                    String author = cursor.getString(cursor.getColumnIndex(ContractClass.Sounds.COLUMN_NAME_AUTHOR));
                    String time = cursor.getString(cursor.getColumnIndex(ContractClass.Sounds.COLUMN_NAME_TIME));
                    String url = cursor.getString(cursor.getColumnIndex(ContractClass.Sounds.COLUMN_NAME_URL));
                    sounds.add(new Sound(soundName, author, time, url));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return sounds;

    }

}

