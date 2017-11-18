package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import rx.functions.Action1;

/**
 * Created by dmitro on 15.10.17.
 */

public class PlayListsRecyclerAdapter extends RecyclerView.Adapter<PlayListsRecyclerAdapter.ChannelHolder> {
    private ArrayList<Playlist> playlists;
    private Action1<String> onClick;


    public void setOnClick(Action1<String> onClick) {
        this.onClick = onClick;
    }

    public PlayListsRecyclerAdapter(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }


    public void updateData(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();

    }

    public void addMessage(Playlist playlist) {
        playlists.add(playlist);
        notifyDataSetChanged();
    }


    @Override
    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_playlist, parent, false);
        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelHolder holder, int position) {
        holder.bindView(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        public TextView namePlayListTextView;
        private TextView sizeSoundTextView;
//

        public ChannelHolder(View itemView) {
            super(itemView);
            initView();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick != null) {
                        onClick.call(playlists.get(getAdapterPosition()).getId());
                    }
                }
            });

        }

        private void initView() {
            namePlayListTextView = itemView.findViewById(R.id.textViewNamePlayList);
            sizeSoundTextView = itemView.findViewById(R.id.textViewCountSound);

        }

        public void bindView(Playlist message) {
            namePlayListTextView.setText(message.getName());
            sizeSoundTextView.setText(message.getFileCount());


        }


    }
}