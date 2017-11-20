package dmitriiserdun.gmail.com.musickiua.screens.player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.screens.top_songs.TopSongsContract;
import dmitriiserdun.gmail.com.musickiua.screens.top_songs.TopSongsPresenter;
import dmitriiserdun.gmail.com.musickiua.screens.top_songs.TopSongsView;


public class PlayerFragment extends BaseFragment {

    private PlayerContract.View view;
    private PlayerContract.Presenter presenter;


    public PlayerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();

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
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        view = new PlayerView(root, this);
        presenter = new PlayerPresenter(this, view);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event

}
