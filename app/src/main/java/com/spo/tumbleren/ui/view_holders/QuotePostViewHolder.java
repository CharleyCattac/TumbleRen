package com.spo.tumbleren.ui.view_holders;

import android.view.View;
import android.widget.TextView;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.tumblr.jumblr.types.QuotePost;

import java.util.Locale;

public class QuotePostViewHolder extends PostViewHolder<QuotePost> {
    private TextView text;
    private TextView source;

    public QuotePostViewHolder(View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.quote_text);
        source = itemView.findViewById(R.id.quote_source);
    }

    @Override
    public void bind(final QuotePost quotePost, final PostAdapter.OnItemClickListener listener) {
        super.bind(quotePost, listener);

        try {
            String tmpSource = quotePost.getSourceTitle();
            if (tmpSource == null){
                reblogSign.setVisibility(View.GONE);
                initBlogName.setText(quotePost.getBlogName());
            }
            else
                initBlogName.setText(quotePost.getSourceTitle());
            String creationDateFormatted = getFormattedDate(quotePost.getDateGMT());
            creationDate.setText(creationDateFormatted);

            String tmpText = "\"" + quotePost.getText() + "\"";
            text.setText(tmpText);

            source.setText("— " + tmpSource);

            sourceUrl.setText(quotePost.getPostUrl());


            String tmpTags = String.join(", ", quotePost.getTags());
            tags.setText(tmpTags);
            tags.setVisibility(tmpTags.isEmpty() ? View.GONE : View.VISIBLE);


            notesAmount.setText(String.format(Locale.getDefault(), "%d", quotePost.getNoteCount()));
        } catch (Exception ex){

        }

    }

}
