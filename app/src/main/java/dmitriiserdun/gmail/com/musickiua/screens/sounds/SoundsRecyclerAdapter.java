package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action1;

/**
 * Created by dmitro on 15.10.17.
 */

public class SoundsRecyclerAdapter extends RecyclerView.Adapter<SoundsRecyclerAdapter.ChannelHolder> {
    private ArrayList<Sound> sounds;
    private Action1<String> onClick;


    public void setOnClick(Action1<String> onClick) {
        this.onClick = onClick;
    }

    public SoundsRecyclerAdapter(ArrayList<Sound> sounds) {
        this.sounds = sounds;
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
        private Button play;
//

        public ChannelHolder(View itemView) {
            super(itemView);
            initView();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick != null) {
                        onClick.call(sounds.get(getAdapterPosition()).getUrl());
                    }
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
////mg15.i.ua/g/4fdaf08fe3f1a9079505e5171cc17234/5a0d7817/music21/1/4/1125041
////                        String url = "storage/emulated/0/audio.mp3";
////                        String fileName = Uri.parse(url).getLastPathSegment();
////                        File file = File.createTempFile(fileName, null, App.getInstance().getCacheDir());
////                        MediaPlayer mPlayer = MediaPlayer.create(App.getInstance(), "");
////                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////
////                        mPlayer.start();
//                        mMediaPlayer.setDataSource("http://online-radioroks.tavrmedia.ua/RadioROKS");
//                        mMediaPlayer.prepareAsync();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

//                    final MediaPlayer    mMediaPlayer = new MediaPlayer();
//
//                    try {
//                        mMediaPlayer.setDataSource("http://mg15.i.ua/g/4fdaf08fe3f1a9079505e5171cc17234/5a0d7817/music21/1/4/1125041");
//                        mMediaPlayer.prepareAsync();
//
//                        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//                            public void onPrepared(MediaPlayer mp) {
//                                mMediaPlayer.start();
//                            }
//                        });

//                        notifyCallbackListeners(PlayerCallbackEvent.PLAYER_SONG_CHANGE);
//                    }
//                    catch (IllegalArgumentException e) {
//                        e.printStackTrace();
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (IllegalStateException ex){
//                        ex.printStackTrace();
//                    }
                }
            });

        }

        private void initView() {
            name = itemView.findViewById(R.id.soundNameTextView);
            author = itemView.findViewById(R.id.authorTextView);
            time = itemView.findViewById(R.id.timeTextView);
            play = itemView.findViewById(R.id.play);

        }

        public void bindView(Sound message) {
            name.setText(message.getName());
            author.setText(message.getAuthor());
            time.setText(message.getTime());


        }


    }
}