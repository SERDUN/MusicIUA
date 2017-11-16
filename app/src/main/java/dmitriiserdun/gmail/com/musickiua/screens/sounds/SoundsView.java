package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class SoundsView implements SoundsContract.View {

    private RecyclerView recyclerView;
    private SoundsRecyclerAdapter soundsRecyclerAdapter;

    private View root;
    private BaseActivity activity;

    private Button button;




    public SoundsView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initView();
    }

    private void initView() {
        recyclerView=root.findViewById(R.id.recyclerViewSounds);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity.getBaseContext()));
        soundsRecyclerAdapter =new SoundsRecyclerAdapter(new ArrayList<Sound>());
        recyclerView.setAdapter(soundsRecyclerAdapter);

    }

    @Override
    public void setPresenter(SoundsContract.Presenter presenter) {


    }


    @Override
    public void addPlayListsInList(ArrayList<Sound> playlists) {
        soundsRecyclerAdapter.updateData(playlists);
    }

    @Override
    public void showMessage(int id) {
        Toast.makeText(activity, id, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Context getContext() {
        return activity.getBaseContext();
    }

    @Override
    public void onClickListener(Action1<String> action0) {
        soundsRecyclerAdapter.setOnClick(action0);
    }




}
