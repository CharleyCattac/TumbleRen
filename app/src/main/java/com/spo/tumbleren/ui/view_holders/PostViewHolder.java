package com.spo.tumbleren.ui.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostViewHolder<T> extends RecyclerView.ViewHolder {
    private static final String TUMBLER_RESPONSE_FORMAT ="yyyy-MM-dd HH:mm:ss";
    private static final String MONTH_DAY_FORMAT = "MMM d, yyyy"; // Oct 26, 2018

    protected ImageView blogAvatar;
    protected TextView initBlogName;
    protected TextView reblogSign;
    protected TextView creationDate;
    protected TextView sourceUrl;
    protected TextView tags;
    protected TextView notesAmount;

    protected PostViewHolder(View itemView) {
        super(itemView);
        blogAvatar = itemView.findViewById(R.id.post_blog_avatar);
        initBlogName = itemView.findViewById(R.id.post_blog_name);
        reblogSign = itemView.findViewById(R.id.post_reblog_sigh);
        creationDate = itemView.findViewById(R.id.date);
        sourceUrl = itemView.findViewById(R.id.source_url);
        tags = itemView.findViewById(R.id.tags);
        notesAmount = itemView.findViewById(R.id.post_notes_amount);
    }

    public void bind(final T post, final PostAdapter.OnItemClickListener listener){
        View.OnClickListener blogJump = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(initBlogName.getText().toString());
            }
        };
        blogAvatar.setOnClickListener(blogJump);
        initBlogName.setOnClickListener(blogJump);
        View.OnClickListener postJump = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(sourceUrl.getText().toString());
            }
        };
        sourceUrl.setOnClickListener(postJump);
    }

    protected String getFormattedDate(String rawDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(TUMBLER_RESPONSE_FORMAT, Locale.ROOT);
        SimpleDateFormat displayedFormat = new SimpleDateFormat(MONTH_DAY_FORMAT, Locale.ENGLISH);
        try {
            Date date = utcFormat.parse(rawDate);
            return displayedFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
