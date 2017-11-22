package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.player.ControlPlayer;
import dmitriiserdun.gmail.com.musickiua.base.player.SPlayerView;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action2;

/**
 * Created by dmitro on 31.10.17.
 */

public class SoundsView implements SoundsContract.View {

    private RecyclerView recyclerView;
    private SoundsRecyclerAdapter soundsRecyclerAdapter;

    private View root;
    private BaseActivity activity;


    private SPlayerView sPlayerView;

    private void initPlayer() {
        sPlayerView = root.findViewById(R.id.player);
    }

    public SoundsView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initPlayer();
        initView();
    }

    private void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getBaseContext());
        recyclerView = root.findViewById(R.id.recyclerViewSounds);
        recyclerView.setLayoutManager(layoutManager);
        soundsRecyclerAdapter = new SoundsRecyclerAdapter(new ArrayList<Sound>());
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
    public void setOnItemListListener(Action2<Sound, Integer> action0) {
        soundsRecyclerAdapter.setOnClick(action0);
    }


    @Override
    public void initControllerWithPlayer(ControlPlayer controlPlayer) {
        sPlayerView.initController(controlPlayer);
    }

}
