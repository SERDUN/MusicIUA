package dmitriiserdun.gmail.com.musickiua.model;

import java.util.ArrayList;

/**
 * Created by dmitro on 20.11.17.
 */

public class FoundSounds {
    private ArrayList<Sound> sounds;
    private String currentPage;
    private String maxPage;

    public FoundSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public FoundSounds(ArrayList<Sound> sounds, String currentPage, String maxPage) {
        this.sounds = sounds;
        this.currentPage = currentPage;
        this.maxPage = maxPage;
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(String maxPage) {
        this.maxPage = maxPage;
    }
}
