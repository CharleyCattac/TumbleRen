package com.spo.tumbleren.ui.view_holders;

import android.view.View;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.Post;

import java.util.Locale;

public class UnknownPostViewHolder extends PostViewHolder<Post> {

    public UnknownPostViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(final Post post, final PostAdapter.OnItemClickListener listener) {
        super.bind(post, listener);

        String tmpSource = post.getSourceTitle();
        if (tmpSource == null){
            reblogSign.setVisibility(View.GONE);
            initBlogName.setText(post.getBlogName());
        }
        else
            initBlogName.setText(post.getSourceTitle());

        String creationDateFormatted = getFormattedDate(post.getDateGMT());
        creationDate.setText(creationDateFormatted);

        sourceUrl.setText(post.getPostUrl());


        String tmpTags = String.join(", ", post.getTags());
        tags.setText(tmpTags);
        tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

        notesAmount.setText(String.format(Locale.getDefault(),"%d", post.getNoteCount()));
    }

}
