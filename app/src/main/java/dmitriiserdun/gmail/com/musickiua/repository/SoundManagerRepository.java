package dmitriiserdun.gmail.com.musickiua.repository;

import java.util.List;

import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by dmitro on 15.11.17.
 */

public class SoundManagerRepository implements SoundRepository {
    private static SoundManagerRepository INSTANCE = null;

    private final SoundRepository remoteSoundRepository;


    private SoundManagerRepository(@NonNull SoundRepository remoteSoundRepository) {
        this.remoteSoundRepository = remoteSoundRepository;
    }

    public static SoundManagerRepository getInstance(SoundRepository tasksRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new SoundManagerRepository(tasksRemoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<Integer> login(String login, String pass) {
        return remoteSoundRepository.login(login, pass);
    }

    @Override
    public Observable<List<Playlist>> getPlaylists(Integer userId) {
        return remoteSoundRepository.getPlaylists(userId);
    }

    @Override
    public Observable<List<Sound>> getSounds(Integer userId, String playlistId) {
        return remoteSoundRepository.getSounds(userId, playlistId);
    }

    @Override
    public Observable<ResponseBody> getSounds(String url) {
        return remoteSoundRepository.getSounds(url);
    }
}
