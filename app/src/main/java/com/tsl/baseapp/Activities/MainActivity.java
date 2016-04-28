package com.tsl.baseapp.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.rey.material.widget.ProgressView;
import com.tsl.baseapp.Controller.Controller;
import com.tsl.baseapp.model.Adapters.ProjectsAdapter;
import com.tsl.baseapp.model.Objects.project.Project;
import com.tsl.baseapp.model.Utilities.EndlessRecyclerOnScrollListener;
import com.tsl.baseapp.R;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements Controller.ProjectsCallBackListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.progressBar)
    ProgressView mProgressBar;
    private List<Project> mProjectList = new ArrayList<>();
    private ProjectsAdapter mProjectsAdapter;
    private Controller mController;
    private LinearLayoutManager lm;
    private EndlessRecyclerOnScrollListener projectsPaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        lm = new LinearLayoutManager(this);
        mProjectsAdapter = new ProjectsAdapter(mProjectList, this);
        mController = new Controller(MainActivity.this);
        addList();
        //mController.startFetchingProjects(Constants.isUser(this), 1);

        projectsPaging = new EndlessRecyclerOnScrollListener(lm, mProjectsAdapter) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("PAGE:: ", String.valueOf(current_page));
                //mController.startFetchingProjects(Constants.isUser(MainActivity.this), current_page);
                mProjectsAdapter.notifyDataSetChanged();
            }
        };

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming Soon", Snackbar.LENGTH_LONG);
            }
        });
    }

    private void addList() {
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mProjectsAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProjectsAdapter.removeProjects();
                mProjectsAdapter.notifyDataSetChanged();
                //mController.startFetchingProjects(Constants.isUser(MainActivity.this), 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchStart() {
        mProgressBar.start();
        mRecyclerView.clearOnScrollListeners();
    }

    @Override
    public void onFetchProgress(Project project) {
        mProjectsAdapter.addProjects(project);
    }

    @Override
    public void onFetchProgress(List<Project> projectList) {

    }

    @Override
    public void onFetchComplete() {
        mRecyclerView.addOnScrollListener(projectsPaging);
        mProgressBar.stop();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFetchFailed() {
        mProgressBar.stop();
        if (mProjectsAdapter.getItemCount() == 0) {
            Toast.makeText(this, "No projects under this category", Toast.LENGTH_SHORT).show();
        }

    }
}
