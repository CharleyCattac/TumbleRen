package com.spo.tumbleren.ui.view_holders;

import android.view.View;
import android.widget.TextView;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.LinkPost;

import java.util.Locale;

public class LinkPostViewHolder extends PostViewHolder<LinkPost> {
    private TextView link;
    private TextView title;
    private TextView description;

    public LinkPostViewHolder(View itemView) {
        super(itemView);
        link = itemView.findViewById(R.id.link_url);
        title = itemView.findViewById(R.id.link_title);
        description = itemView.findViewById(R.id.link_description);
    }

    @Override
    public void bind(final LinkPost linkPost, final PostAdapter.OnItemClickListener listener) {
        super.bind(linkPost, listener);

        try {
            String tmpSource = linkPost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(linkPost.getBlogName());
            }
            else
                initBlogName.setText(linkPost.getSourceTitle());

            String creationDateFormatted = getFormattedDate(linkPost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            link.setText(linkPost.getLinkUrl());
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(linkPost);
                }
            });

            String tmpTitle = linkPost.getTitle();
            title.setText(tmpTitle);
            title.setVisibility(tmpTitle.isEmpty() ? View.GONE : View.VISIBLE);

            String tmpDescription = linkPost.getDescription();
            description.setText(tmpDescription);
            description.setVisibility(tmpDescription.isEmpty() ? View.GONE : View.VISIBLE);

            sourceUrl.setText(linkPost.getPostUrl());

            String tmpTags = String.join(", ", linkPost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);

            notesAmount.setText(String.format(Locale.getDefault(), "%d", linkPost.getNoteCount()));
        } catch (Exception ex){

        }

    }

}
