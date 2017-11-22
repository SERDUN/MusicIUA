package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class PlayListView implements PlayListContract.View {

    private RecyclerView recyclerView;
    private PlayListsRecyclerAdapter playListsRecyclerAdapter;

    private View root;
    private BaseFragment baseFragment;
    private Toolbar toolbar;



    public PlayListView(View root, BaseFragment baseFragment) {
        this.root = root;
        this.baseFragment = baseFragment;
        initView();
        initToolbar();
    }

    private void initToolbar() {
        toolbar = baseFragment.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("My Playlists");

        Menu currentMenu = toolbar.getMenu();
        if (currentMenu != null) {
            currentMenu.clear();
        }
        // toolbar.inflateMenu(R.menu.navigation);

    }

    private void initView() {
        recyclerView = root.findViewById(R.id.recyclerViewPlayLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(baseFragment.getContext()));
        playListsRecyclerAdapter = new PlayListsRecyclerAdapter(new ArrayList<Playlist>());
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
        Toast.makeText(baseFragment.getContext(), id, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Context getContext() {
        return baseFragment.getContext();
    }

    @Override
    public void onClickListener(Action1<String> action0) {
        playListsRecyclerAdapter.setOnClick(action0);
    }



}
