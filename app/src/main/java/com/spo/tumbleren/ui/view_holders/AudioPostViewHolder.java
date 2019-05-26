package com.spo.tumbleren.ui.view_holders;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.AudioPost;

import java.io.IOException;
import java.util.Locale;

public class AudioPostViewHolder extends PostViewHolder<AudioPost> {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    private TextView caption;
    private ImageView audioSign;
    private TextView songName;
    private TextView artistName;

    public AudioPostViewHolder(View itemView) {
        super(itemView);
        caption = itemView.findViewById(R.id.audio_caption);
        audioSign = itemView.findViewById(R.id.audio_sign);
        songName = itemView.findViewById(R.id.song_name);
        artistName = itemView.findViewById(R.id.artist_name);
    }

    private void setMediaSource(String trackURL) throws IOException {
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(trackURL);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioSign.setImageResource(R.drawable.play64);
            }
        });
    }

    @Override
    public void bind(final AudioPost audioPost, final PostAdapter.OnItemClickListener listener) {
        super.bind(audioPost, listener);

        try {
            String tmpSource = audioPost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(audioPost.getBlogName());
            }
            else
                initBlogName.setText(audioPost.getSourceTitle());

            String creationDateFormatted = getFormattedDate(audioPost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            String tmpTitle = audioPost.getCaption();
            caption.setText(tmpTitle);
            caption.setVisibility(tmpTitle.isEmpty() ? View.GONE : View.VISIBLE);

            audioSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isPlaying) {
                        isPlaying = false;
                        mediaPlayer.pause();
                        audioSign.setImageResource(R.drawable.play64);
                    }
                    else{
                        isPlaying = true;
                        mediaPlayer.start();
                        audioSign.setImageResource(R.drawable.pause64);
                    }

                    listener.onItemClick(audioPost);
                }
            });

            if (audioPost.getArtistName() != null) {
                String tmpArtist = audioPost.getArtistName();
                songName.setText(tmpArtist);
                songName.setVisibility(tmpArtist.isEmpty() ? View.GONE : View.VISIBLE);
            }
            else
                songName.setVisibility(View.GONE);
            if (audioPost.getTrackName() != null) {
                String tmpTrack = audioPost.getTrackName();
                artistName.setText(tmpTrack);
                artistName.setVisibility(tmpTrack.isEmpty() ? View.GONE : View.VISIBLE);
            }
            else
                artistName.setVisibility(View.GONE);

            if (audioPost.getAudioUrl() != null)
                setMediaSource(audioPost.getAudioUrl());

            sourceUrl.setText(audioPost.getPostUrl());

            String tmpTags = String.join(", ", audioPost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

            notesAmount.setText(String.format(Locale.getDefault(), "%d", audioPost.getNoteCount()));
        } catch (Exception ex){

        }

    }

}
