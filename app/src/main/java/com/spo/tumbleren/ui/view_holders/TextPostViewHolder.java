package com.spo.tumbleren.ui.view_holders;

import android.view.View;
import android.widget.TextView;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.TextPost;

import java.util.Locale;

public class TextPostViewHolder extends PostViewHolder<TextPost> {
    private TextView title;
    private TextView summary;

    public TextPostViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.text_title);
        summary = itemView.findViewById(R.id.text_summary);
    }

    @Override
    public void bind(final TextPost textPost, final PostAdapter.OnItemClickListener listener) {
        super.bind(textPost, listener);

        try {
            String tmpSource = textPost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(textPost.getBlogName());
            }
            else
                initBlogName.setText(textPost.getSourceTitle());

            String creationDateFormatted = getFormattedDate(textPost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            String tmpTitle = textPost.getTitle();
            title.setText(tmpTitle);
            title.setVisibility(tmpTitle.isEmpty() ? View.GONE : View.VISIBLE);

            summary.setText(textPost.getBody());

            sourceUrl.setText(textPost.getPostUrl());

            String tmpTags = String.join(", ", textPost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

            notesAmount.setText(String.format(Locale.getDefault(), "%d", textPost.getNoteCount()));
        } catch (Exception ex){

        }

    }

}
