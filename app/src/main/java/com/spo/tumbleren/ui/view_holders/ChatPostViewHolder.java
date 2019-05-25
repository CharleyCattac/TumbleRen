package com.spo.tumbleren.ui.view_holders;

import android.view.View;
import android.widget.TextView;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.ChatPost;

import java.util.Locale;

public class ChatPostViewHolder extends PostViewHolder<ChatPost> {
    private TextView title;
    private TextView body;

    public ChatPostViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.dialog_title);
        body = itemView.findViewById(R.id.dialog_body);
    }

    @Override
    public void bind(final ChatPost chatPost, final PostAdapter.OnItemClickListener listener) {
        super.bind(chatPost, listener);

        try {
            String tmpSource = chatPost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(chatPost.getBlogName());
            }
            else
                initBlogName.setText(chatPost.getSourceTitle());

            String creationDateFormatted = getFormattedDate(chatPost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            String tmpTitle = chatPost.getTitle();
            title.setText(tmpTitle);
            title.setVisibility(tmpTitle.isEmpty() ? View.GONE : View.VISIBLE);

            body.setText(chatPost.getBody());

            sourceUrl.setText(chatPost.getPostUrl());

            String tmpTags = String.join(", ", chatPost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

            notesAmount.setText(String.format(Locale.getDefault(), "%d", chatPost.getNoteCount()));
        } catch (Exception ex){

        }

    }

}
