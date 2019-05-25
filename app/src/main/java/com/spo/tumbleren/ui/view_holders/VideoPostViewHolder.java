package com.spo.tumbleren.ui.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.VideoPost;

import java.util.Locale;

public class VideoPostViewHolder extends PostViewHolder<VideoPost> {
    private TextView caption;

    private ImageView videoSign;
    private ImageView thumbnail;

    public VideoPostViewHolder(View itemView) {
        super(itemView);
        caption = itemView.findViewById(R.id.video_caption);

        videoSign = itemView.findViewById(R.id.video_sign);
        thumbnail = itemView.findViewById(R.id.video_thumbnail);

    }

    @Override
    public void bind(final VideoPost videoPost, final PostAdapter.OnItemClickListener listener) {
        super.bind(videoPost, listener);

        try {
            String tmpSource = videoPost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(videoPost.getBlogName());
            }
            else
                initBlogName.setText(videoPost.getSourceTitle());

            String creationDateFormatted = getFormattedDate(videoPost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            String tmpTitle = videoPost.getCaption();
            caption.setText(tmpTitle);
            caption.setVisibility(tmpTitle.isEmpty() ? View.GONE : View.VISIBLE);


            videoSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(videoPost);
                }
            });

            int origHeight = videoPost.getThumbnailHeight();
            int origWidth = videoPost.getThumbnailHeight();
            int divider = 1;
            while (origWidth > R.dimen.max_image_size){
                origHeight*=divider;
                origWidth*=divider;
                divider+=0.5;
                origHeight/=divider;
                origWidth/=divider;
            }

            Picasso.get().load(videoPost.getThumbnailUrl())
                    .resize(origWidth, origHeight)
                    .into(thumbnail);

            sourceUrl.setText(videoPost.getPostUrl());

            String tmpTags = String.join(", ", videoPost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

            notesAmount.setText(String.format(Locale.getDefault(), "%d", videoPost.getNoteCount()));
        } catch (Exception ex){

        }

    }



}
