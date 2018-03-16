package com.tsl.baseapp.feed;

import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tsl.baseapp.R;
import com.tsl.baseapp.model.objects.user.User;

import java.util.List;
import timber.log.Timber;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class FeedAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public FeedAdapter(List<User> userList){
        super(R.layout.card_item, userList);
    }

    @Override
    protected void convert(BaseViewHolder holder, User user) {
        //some users in baseapp don't have names so we can set it ourselves
        String firstName = user.getFirst_name();
        String lastName = user.getLast_name();
        if (firstName.equals("")){
            firstName = "First Name";
        }
        if (lastName.equals("")){
            lastName = "Last Name";
        }
        holder.setText(R.id.user_first_name, firstName);
        holder.setText(R.id.user_last_name, lastName);

        Glide.with(mContext)
                .load(user.getUserImages().getSmall())
                .into((AppCompatImageView) holder.getView(R.id.user_small_image));
        Glide.with(mContext)
                .load(user.getUserImages().getFull_size())
                .into((AppCompatImageView) holder.getView(R.id.user_large_image));

    }
}
