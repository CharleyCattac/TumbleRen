package com.spo.tumbleren.ui.view_holders;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PhotoAdapter;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;

import java.util.Locale;

public class PhotoPostViewHolder extends PostViewHolder<PhotoPost> {
    private TextView caption;
    private RecyclerView photoView;
    private PhotoAdapter photoAdapter;

    public PhotoPostViewHolder(View itemView) {
        super(itemView);
        caption = itemView.findViewById(R.id.photo_caption);
        photoView = itemView.findViewById(R.id.pending_photos);
        initRecyclerView(itemView);
    }

    private void initRecyclerView(final View itemView) {

        ViewCompat.setNestedScrollingEnabled(photoView, false);
        photoView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.VERTICAL, false));
        photoAdapter = new PhotoAdapter(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Photo photo) {
                try {
                    Toast.makeText(itemView.getContext(), photo.getOriginalSize().getUrl(), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(itemView.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        photoView.setAdapter(photoAdapter);
    }

    @Override
    public void bind(final PhotoPost photoPost, final PostAdapter.OnItemClickListener listener) {
        super.bind(photoPost, listener);

        try {
            String tmpSource = photoPost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(photoPost.getBlogName());
            }
            else
                initBlogName.setText(photoPost.getSourceTitle());

            String creationDateFormatted = getFormattedDate(photoPost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            String tmpTitle = photoPost.getCaption();
            caption.setText(tmpTitle);
            caption.setVisibility(tmpTitle.isEmpty() ? View.GONE : View.VISIBLE);

            photoAdapter.setItems(photoPost.getPhotos());

            sourceUrl.setText(photoPost.getPostUrl());

            String tmpTags = String.join(", ", photoPost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

            notesAmount.setText(String.format(Locale.getDefault(), "%d", photoPost.getNoteCount()));
        } catch (Exception ex){

        }

    }

}
