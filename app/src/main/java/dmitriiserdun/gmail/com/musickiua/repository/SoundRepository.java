package dmitriiserdun.gmail.com.musickiua.repository;

import dmitriiserdun.gmail.com.musickiua.model.User;
import io.reactivex.annotations.NonNull;
import rx.Observable;

/**
 * Created by dmitro on 15.11.17.
 */

public interface SoundRepository {
    @NonNull
    Observable<Integer> login(String login,String pass);

}
