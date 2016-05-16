package com.tsl.baseapp.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsl.baseapp.R;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;
    public ImageView mImageView;

    public FeedViewHolder(View itemView) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(R.id.cardText);
        mImageView = (ImageView) itemView.findViewById(R.id.cardImage);
    }
}
