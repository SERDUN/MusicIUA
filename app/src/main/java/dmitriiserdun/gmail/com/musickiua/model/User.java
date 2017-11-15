package dmitriiserdun.gmail.com.musickiua.model;

/**
 * Created by dmitro on 15.11.17.
 */

public class User {
    public String name;
    public String id;
    public String photoUrl;

    public User(String name, String id, String photoUrl) {
        this.name = name;
        this.id = id;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
