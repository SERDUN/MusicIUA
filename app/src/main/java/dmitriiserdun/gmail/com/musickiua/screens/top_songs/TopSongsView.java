package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.screens.player.PlayerFragment;
import dmitriiserdun.gmail.com.musickiua.screens.sounds.SoundsRecyclerAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.schedulers.Schedulers;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsView implements TopSongsContract.View {
    private RecyclerView recyclerView;
    private SoundsRecyclerAdapter soundsRecyclerAdapter;

    private View root;
    private BaseFragment baseFragment;
    private Toolbar toolbar;
    private SearchView searchView;
    private ImageButton searchButton;
    private ProgressBar progressBar;
    private PlayerFragment player;
    private FrameLayout containerPlayer;


    public TopSongsView(View root, BaseFragment baseFragment) {
        this.root = root;
        this.baseFragment = baseFragment;
        initToolbar();
        initView();
        initPlayer();
    }

    private void initPlayer() {
        player = (PlayerFragment) baseFragment.getChildFragmentManager().findFragmentById(R.id.fragment_player);
        Log.d("sds", "initPlayer: ");

    }

    private void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(baseFragment.getContext());
        recyclerView = root.findViewById(R.id.recyclerViewSounds);
        recyclerView.setLayoutManager(layoutManager);
        soundsRecyclerAdapter = new SoundsRecyclerAdapter(new ArrayList<Sound>());
        recyclerView.setAdapter(soundsRecyclerAdapter);
        searchButton = root.findViewById(R.id.searchButton);
        progressBar = root.findViewById(R.id.progressBar);
        containerPlayer=root.findViewById(R.id.containerPlayer);
    }

    private void initToolbar() {
        toolbar = baseFragment.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Top songs");

        Menu currentMenu = toolbar.getMenu();
        if (currentMenu != null) {
            currentMenu.clear();
        }
        toolbar.inflateMenu(R.menu.navigation);

        searchView = (SearchView) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.toolbar_menu_search));


    }

    @Override
    public void setPresenter(TopSongsContract.Presenter presenter) {

    }

    @Override
    public void addSoundsList(ArrayList<Sound> sounds) {
        soundsRecyclerAdapter.updateData(sounds);
    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public String getSearchingText() {
        return searchView.getQuery().toString();
    }

    @Override
    public Observable<CharSequence> getSearchText() {
        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.toolbar_menu_search).getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        return RxTextView.textChanges(searchEditText)
                .observeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> onClickFind() {
        return RxView.clicks(searchButton);
    }

    @Override
    public void showButtonFind() {
        searchButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonFind() {
        searchButton.setVisibility(View.GONE);

    }

    @Override
    public void showMainLoader(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showUI(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPlayer(boolean isShow) {
        if (isShow) {
            containerPlayer.setVisibility(View.VISIBLE);
        } else {
            containerPlayer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnItemListListener(Action2<Sound, Integer> action0) {
        soundsRecyclerAdapter.setOnClick(action0);
    }


    @Override
    public void hideSearchView() {
        searchView.onActionViewCollapsed();
    }
}
