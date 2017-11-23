package dmitriiserdun.gmail.com.musickiua.model;

import java.io.Serializable;

/**
 * Created by dmitro on 15.11.17.
 */

public class Sound implements Serializable {
    private String name;
    private String author;
    private String soundId;
    private String time;
    private int timeMilis;
    private String url;



    public Sound(String name, String author, String soundId, String time, String url) {
        this.name = name;
        this.author = author;
        this.soundId = soundId;
        this.time = time;
        this.url = url;
    }

    public String getSoundId() {
        return soundId;
    }

    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    public int getTimeMilis() {
        return timeMilis;
    }

    public void setTimeMilis(int timeMilis) {
        this.timeMilis = timeMilis;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sound sound = (Sound) o;

        if (name != null ? !name.equals(sound.name) : sound.name != null) return false;
        if (author != null ? !author.equals(sound.author) : sound.author != null) return false;
        if (time != null ? !time.equals(sound.time) : sound.time != null) return false;
        return url != null ? url.equals(sound.url) : sound.url == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n Sound{" +
                "name='" + name + '\'' +
                '}';
    }
}
