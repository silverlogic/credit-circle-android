package com.tsl.baseapp.model.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsl.baseapp.model.Objects.project.Project;
import com.tsl.baseapp.model.Utilities.Constants;
import com.tsl.baseapp.R;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 9/17/15.
 */
public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.Holder> {

    public static String TAG = ProjectsAdapter.class.getSimpleName();


    private List<Project> mProjects;
    private Context mContext;

    public ProjectsAdapter(List<Project> projects, Context context) {
        mProjects = projects;
        mContext = context;
    }

    public void addProjects(Project project) {
        mProjects.add(project);
        notifyDataSetChanged();
    }

    public void removeProjects() {
        mProjects.clear();
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        String mToken = Constants.getToken(mContext);

        Project currentProject = mProjects.get(position);

        String projectImageUrl = currentProject.mImageUrl;

        String endDate = currentProject.mEndDate;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime endDateFormatted = formatter.parseDateTime(endDate);
        DateTime currentDate = new DateTime();
        Duration duration = new Duration(currentDate, endDateFormatted);
        Long timeLeft = duration.getStandardDays() + 1;
        String daysLeft = timeLeft.toString();


        holder.mProjectName.setText(currentProject.mName);
        holder.mProjectDaysLeftText.setText(daysLeft + " Days Left");
        holder.mProjectTagline.setText(currentProject.mTagline);
        holder.mProjectBlurb.setText(currentProject.mBlurb);

        Picasso.with(mContext)
                .load(projectImageUrl)
                .placeholder(R.drawable.img_feed_center_1)
                .into(holder.mProjectImage);
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.projectName)
        TextView mProjectName;
        @Bind(R.id.projectDaysLeftText)
        TextView mProjectDaysLeftText;
        @Bind(R.id.projectTagline)
        TextView mProjectTagline;
        @Bind(R.id.projectMoneyRaisedText)
        TextView mProjectMoneyRaisedText;
        @Bind(R.id.projectImage)
        ImageView mProjectImage;
        @Bind(R.id.projectBlurb)
        TextView mProjectBlurb;
        @Bind(R.id.card_view)
        CardView mCardView;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
