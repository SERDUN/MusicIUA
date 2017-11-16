package dmitriiserdun.gmail.com.musickiua.repository;

import java.util.List;

import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.model.User;
import io.reactivex.annotations.NonNull;
import rx.Observable;

/**
 * Created by dmitro on 15.11.17.
 */

public interface SoundRepository {
    @NonNull
    Observable<Integer> login(String login,String pass);

    @NonNull
    Observable<List<Playlist>> getPlaylists(Integer userId);

    Observable<List<Sound>> getSounds(Integer userId, String playlistId);

}
