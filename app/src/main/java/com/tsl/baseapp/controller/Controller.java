package com.tsl.baseapp.Controller;

import com.tsl.baseapp.model.Api.ApiManager;
import com.tsl.baseapp.model.Objects.project.Project;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kevin on 9/17/15.
 */
public class Controller {

    private static final String TAG = Controller.class.getSimpleName();
    private ProjectsCallBackListener mListener;
    private ApiManager mApiManager;
    private List<Project> mProjects = new ArrayList<>();

    public Controller(ProjectsCallBackListener listener){
        mListener = listener;
        mApiManager = new ApiManager();
    }
//
//
//    public void startFetchingProjects(String token, int page){
//        mListener.onFetchStart();
//        mApiManager.getAppApi().getProjects(token, page, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                Log.d(TAG, "JSON :: " + s);
//
//                try {
//                    JSONObject json = new JSONObject(s);
//                    JSONArray projects = json.getJSONArray("results");
//                    Log.d(TAG, "JSON ARRAY :: " + projects);
//
//                    for (int i = 0; i < projects.length(); i++) {
//
//                        JSONObject object = projects.getJSONObject(i);
//
//                        JSONObject address = object.getJSONObject("address");
//
//                        final Project project = new Project();
//                        project.setUser(object.getInt("user"));
//                        project.setId(object.getInt("id"));
//                        project.setName(object.getString("name"));
//                        project.setTagline(object.getString("tagline"));
//                        project.setEndDate(object.getString("campaign_end_date"));
//
//                        double totalFunded =  Double.parseDouble(object.getString("total_funded"));;
//                        double goal = object.getDouble("goal");
//                        DecimalFormat tfFormat = new DecimalFormat("0.00");
//                        DecimalFormat gFormat = new DecimalFormat("0.##");
//                        project.setTotalFunded(tfFormat.format(totalFunded));
//                        project.setGoal(gFormat.format(goal));
//
//                        project.setImageUrl(object.getString("image_url"));
//                        project.setAddress(address);
//                        project.setCity(address.getString("city"));
//                        project.setState(address.getString("state"));
//                        project.setCategory(object.getInt("category"));
//                        project.setBlurb(object.getString("blurb"));
//                        project.setSlug(object.getString("slug"));
//                        project.setVoteStatus(object.getString("vote_status"));
//                        project.setCurrency(object.getInt("currency"));
//
//                        mProjects.add(project);
//
//                        mListener.onFetchProgress(project);
//                        mListener.onFetchProgress(mProjects);
//
//                    }
//
//                    mListener.onFetchComplete();
//
//                } catch (JSONException e) {
//                    mListener.onFetchFailed();
//                    Log.d(TAG, "JSON FAILED :: " + e);
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d(TAG, "Error PROJECTS :: " + error.getMessage());
//                mListener.onFetchFailed();
//            }
//        });
//
//    }


    public interface ProjectsCallBackListener{

        void onFetchStart();

        void onFetchProgress(Project project);

        void onFetchProgress(List<Project> projectList);

        void onFetchComplete();

        void onFetchFailed();
    }
}
