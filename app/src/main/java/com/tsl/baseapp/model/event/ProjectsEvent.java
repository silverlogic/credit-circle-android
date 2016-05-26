package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.objects.project.Project;

import java.util.List;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class ProjectsEvent {
    private List<Project> mProjects;

    public ProjectsEvent(List<Project> projectList) {
        mProjects = projectList;
    }

    public List<Project> getProjects() {
        return mProjects;
    }
}
