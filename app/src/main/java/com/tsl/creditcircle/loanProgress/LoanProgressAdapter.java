package com.tsl.creditcircle.loanProgress;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.model.objects.Friend;

import java.util.List;

public class LoanProgressAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {

    public LoanProgressAdapter(List<Friend> friendList){
        super(R.layout.vouched_item, friendList);
    }

    @Override
    protected void convert(BaseViewHolder holder, Friend friend) {
        holder.setText(R.id.name_textView, friend.getName());
        holder.setText(R.id.job_textView, friend.getJob());
        holder.setText(R.id.vouched_amount_value, "$" + friend.getVouchedAmount());
        int stars = friend.getStars();

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

