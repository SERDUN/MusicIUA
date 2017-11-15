package dmitriiserdun.gmail.com.musickiua.model;

/**
 * Created by dmitro on 15.11.17.
 */

public class Playlist {
    private String name;
    private String id;
    private String fileCount;

    public Playlist(String name, String id, String fileCount) {
        this.name = name;
        this.id = id;
        this.fileCount = fileCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileCount() {
        return fileCount;
    }

    public void setFileCount(String fileCount) {
        this.fileCount = fileCount;
    }
}
