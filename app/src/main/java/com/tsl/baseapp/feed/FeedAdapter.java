package com.tsl.baseapp.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tsl.baseapp.R;
import com.tsl.baseapp.model.Objects.project.Project;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    @Inject
    public Context mContext;
    private List<Project> mProjectList = new ArrayList<>();

    public FeedAdapter(Context context){
        mContext = context;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        FeedViewHolder holder = new FeedViewHolder(card);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Project project = mProjectList.get(position);
        holder.mTextView.setText(project.getTagline());
        loadImage(project.getImage_url(), holder.mImageView);
    }

    private void loadImage(String url, ImageView view) {
        Glide.with(mContext)
                .load(url)
                .into(view);
    }


    @Override
    public int getItemCount() {
        return mProjectList.size();
    }

    public void setNewsList(List<Project> list) {
        this.mProjectList = list;
        notifyDataSetChanged();
    }
}
