package com.spo.tumbleren.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spo.tumbleren.R;

import com.spo.tumbleren.ui.view_holders.AudioPostViewHolder;
import com.spo.tumbleren.ui.view_holders.ChatPostViewHolder;
import com.spo.tumbleren.ui.view_holders.LinkPostViewHolder;
import com.spo.tumbleren.ui.view_holders.PhotoPostViewHolder;
import com.spo.tumbleren.ui.view_holders.PostViewHolder;
import com.spo.tumbleren.ui.view_holders.QuotePostViewHolder;
import com.spo.tumbleren.ui.view_holders.TextPostViewHolder;
import com.spo.tumbleren.ui.view_holders.UnknownPostViewHolder;
import com.spo.tumbleren.ui.view_holders.VideoPostViewHolder;
import com.tumblr.jumblr.types.AudioPost;
import com.tumblr.jumblr.types.LinkPost;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.VideoPost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private enum POST_TYPE {
        TYPE_TEXT,
        TYPE_PHOTO,
        TYPE_QUOTE,
        TYPE_LINK,
        TYPE_CHAT,
        TYPE_AUDIO,
        TYPE_VIDEO}
    private List<Post> postList = new ArrayList<>();

    private final OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(String link);
        void onItemClick(LinkPost linkPost);
        void onItemClick(VideoPost videoPost);
        void onItemClick(AudioPost videoPost);
    }

    public PostAdapter(OnItemClickListener listener){
        super();
        this.listener = listener;
    }

    public void setItems(Collection<Post> posts) {
        postList.clear();
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    public void clearItems() {
        postList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (postList.get(position).getType().getValue()){
            case "text": return POST_TYPE.TYPE_TEXT.ordinal();
            case "photo": return POST_TYPE.TYPE_PHOTO.ordinal();
            case "quote": return POST_TYPE.TYPE_QUOTE.ordinal();
            case "link": return POST_TYPE.TYPE_LINK.ordinal();
            case "chat": return POST_TYPE.TYPE_CHAT.ordinal();
            case "audio": return POST_TYPE.TYPE_AUDIO.ordinal();
            case "video": return POST_TYPE.TYPE_VIDEO.ordinal();
            default: return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(postList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.text_post_item, parent, false);
                return new TextPostViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.photo_post_item, parent, false);
                return new PhotoPostViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.quote_post_item, parent, false);
                return new QuotePostViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.link_post_item, parent, false);
                return new LinkPostViewHolder(view);
            case 4:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_post_item, parent, false);
                return new ChatPostViewHolder(view);
            case 5:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.audio_post_item, parent, false);
                return new AudioPostViewHolder(view);
            case 6:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_post_item, parent, false);
                return new VideoPostViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.unknown_post_item, parent, false);
                return new UnknownPostViewHolder(view);
        }
    }

}