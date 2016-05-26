package com.tsl.baseapp.model.objects.project;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class ProjectsResults {

    @SerializedName("results")
    private List<Project> project;

    public List<Project> getProjects() {
        return project;
    }

    public void setProjects(List<Project> project) {
        this.project = project;
    }
}
