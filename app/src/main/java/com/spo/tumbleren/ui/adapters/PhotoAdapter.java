package com.spo.tumbleren.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.view_holders.item_view_holders.PhotoViewHolder;
import com.tumblr.jumblr.types.Photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {


    private final OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Photo photo);
    }

    public PhotoAdapter(OnItemClickListener listener){
        super();
        this.listener = listener;
    }

    private List<Photo> photoList = new ArrayList<>();
    public void setItems(Collection<Photo> photos) {
        photoList.clear();
        photoList.addAll(photos);
        notifyDataSetChanged();
    }

    public void clearItems() {
        photoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(photoList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(view);
    }

}