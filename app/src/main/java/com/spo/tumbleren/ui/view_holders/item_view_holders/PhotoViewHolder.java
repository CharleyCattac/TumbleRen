package com.spo.tumbleren.ui.view_holders.item_view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PhotoAdapter;
import com.tumblr.jumblr.types.Photo;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.content_image);
    }

    public void bind(final Photo photo, final PhotoAdapter.OnItemClickListener listener) {
        try {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(photo);
                }
            });

            int origHeight = photo.getOriginalSize().getHeight();
            int origWidth = photo.getOriginalSize().getWidth();
            int divider = 1;
            while (origWidth > R.dimen.max_image_size){
                origHeight*=divider;
                origWidth*=divider;
                divider+=0.5;
                origHeight/=divider;
                origWidth/=divider;
            }

            Picasso.get().load(photo.getOriginalSize().getUrl())
                    .resize(origWidth, origHeight)
                    .into(imageView);
        } catch (Exception ex){

        }
    }

}
