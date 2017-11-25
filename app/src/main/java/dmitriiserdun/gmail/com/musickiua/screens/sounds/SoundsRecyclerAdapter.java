package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action2;

/**
 * Created by dmitro on 15.10.17.
 */

public class SoundsRecyclerAdapter extends RecyclerView.Adapter<SoundsRecyclerAdapter.ChannelHolder> {
    private ArrayList<Sound> sounds;
    private Action2<Sound, Integer> onClick;
    private int selectedItem;


    public void setOnClick(Action2<Sound, Integer> onClick) {
        this.onClick = onClick;
    }

    public SoundsRecyclerAdapter(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Sound> sounds) {
        this.sounds = sounds;
        notifyDataSetChanged();

    }

    public void addMessage(Sound playlist) {
        sounds.add(playlist);
        notifyDataSetChanged();
    }


    @Override
    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_sound, parent, false);
        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelHolder holder, int position) {
        holder.bindView(sounds.get(position));
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        public TextView name;
        private TextView author;
        private TextView time;
      //  private LinearLayoutCompat conteiner;
//

        public ChannelHolder(View itemView) {
            super(itemView);
            initView();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.call(sounds.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        private void initView() {
            name = itemView.findViewById(R.id.soundNameTextView);
            author = itemView.findViewById(R.id.authorTextView);
            time = itemView.findViewById(R.id.timeTextView);
           // conteiner = itemView.findViewById(R.id.itemRv);

        }

        public void bindView(Sound message) {
            if (selectedItem == message.hashCode()) {
               // conteiner.setBackgroundColor(App.getInstance().getResources().getColor(R.color.selectedAudio));
            } else {
               // conteiner.setBackgroundColor(App.getInstance().getResources().getColor(R.color.white));
            }
            name.setText(message.getName());
            author.setText(message.getAuthor());
            time.setText(message.getTime());
            if (getAdapterPosition() == sounds.size() - 1) {
                author.setPadding(0, 0, 0, 240);
            } else {
                author.setPadding(0, 0, 0, 5);

            }


        }


    }
}