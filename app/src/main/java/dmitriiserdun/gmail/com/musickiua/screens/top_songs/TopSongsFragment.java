package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListPresenter;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListView;

public class TopSongsFragment extends BaseFragment {
    public final static String KEY_FRAGMENT = TopSongsFragment.class.getName();
    private TopSongsContract.View view;
    private TopSongsContract.Presenter presenter;


    public TopSongsFragment() {
        // Required empty public constructor
    }


    public static TopSongsFragment newInstance() {
        TopSongsFragment fragment = new TopSongsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_top_songs, container, false);
        view = new TopSongsView(root, this);
        presenter = new TopSongsPresenter(view, this);
        return root;
    }


}
