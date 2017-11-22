package dmitriiserdun.gmail.com.musickiua.screens.navigation;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListFragment;
import dmitriiserdun.gmail.com.musickiua.screens.top_songs.TopSongsFragment;

/**
 * Created by dmitro on 19.11.17.
 */

public class NavView implements NavContract.View, NavigationView.OnNavigationItemSelectedListener {
    private View root;
    private BaseActivity activity;
    Toolbar toolbar;

    public NavView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initView();
    }

    private void initView() {
       // Toolbar toolbar = root.findViewById(R.id.toolbar);
       // activity.setSupportActionBar(toolbar);

        toolbar=activity.getActivityToolbar();
        toolbar.setTitle("My playlists");

        Menu currentMenu = toolbar.getMenu();
        if (currentMenu != null) {
            currentMenu.clear();
        }
        DrawerLayout drawer = root.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = root.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //toolbar.inflateMenu(R.menu.navigation);


//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return true;
//            }
//        });
    }

    @Override
    public void setPresenter(NavContract.Presenter presenter) {

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_playlists) {
            activity.addFragment(PlayListFragment.newInstance(), PlayListFragment.KEY_FRAGMENT);

        } else if (id == R.id.nav_top_songs) {
            activity.addFragment(TopSongsFragment.newInstance(), TopSongsFragment.KEY_FRAGMENT);


        }


        DrawerLayout drawer = (DrawerLayout) root.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
