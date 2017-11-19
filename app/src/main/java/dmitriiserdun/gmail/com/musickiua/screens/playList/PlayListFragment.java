package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.screens.top_songs.TopSongsFragment;

public class PlayListFragment extends BaseFragment {
    public final static String KEY_FRAGMENT = PlayListFragment.class.getName();

    private PlayListContract.View view;
    private PlayListContract.Presenter presenter;

    public PlayListFragment() {
    }


    public static PlayListFragment newInstance() {
        PlayListFragment fragment = new PlayListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_play_list, container, false);
        view = new PlayListView(root, this);
        presenter = new PlayListPresenter(this, view);
        return root;
    }


}
