package dmitriiserdun.gmail.com.musickiua.model;

/**
 * Created by dmitro on 15.11.17.
 */

public class Sound {
    private String name;
    private String author;
    private String time;
    private String url;

    public Sound(String name, String author, String time, String url) {
        this.name = name;
        this.author = author;
        this.time = time;
        this.url = url;
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
}
