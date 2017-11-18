package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class PlayListView implements PlayListContract.View {

    private RecyclerView recyclerView;
    private PlayListsRecyclerAdapter playListsRecyclerAdapter;

    private View root;
    private BaseActivity activity;




    public PlayListView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initView();
    }

    private void initView() {
        recyclerView=root.findViewById(R.id.recyclerViewPlayLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity.getBaseContext()));
        playListsRecyclerAdapter=new PlayListsRecyclerAdapter(new ArrayList<Playlist>());
        recyclerView.setAdapter(playListsRecyclerAdapter);

    }

    @Override
    public void setPresenter(PlayListContract.Presenter presenter) {


    }


    @Override
    public void addPlayListsInList(ArrayList<Playlist> playlists) {
        playListsRecyclerAdapter.updateData(playlists);
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
        playListsRecyclerAdapter.setOnClick(action0);
    }


}
