package com.tsl.money2020.inviteVouchers;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tsl.money2020.R;
import com.tsl.money2020.model.objects.Friend;

import java.util.List;

public class InviteAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {

    public InviteAdapter(List<Friend> friendList){
        super(R.layout.friends_list_item, friendList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Friend friend) {
        holder.setText(R.id.name_textView, friend.getName());
        holder.setText(R.id.job_textView, friend.getJob());
        holder.addOnClickListener(R.id.invite_user_image);
        int stars = friend.getStars();

        if (!friend.isInvited()){
            holder.setImageResource(R.id.invite_user_image, R.drawable.ic_add_user);
        }
        else {
            holder.setImageResource(R.id.invite_user_image, R.drawable.check_circle);
        }

        holder.setVisible(R.id.star4, true);
        holder.setVisible(R.id.star4, true);
        holder.setVisible(R.id.star4, true);
        if (stars < 4) {
            holder.setVisible(R.id.star4, false);
            if (stars< 3) {
                holder.setVisible(R.id.star3, false);
                if (stars< 2) {
                    holder.setVisible(R.id.star2, false);
                }
            }
        }

    }
}
