package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.Objects.project.Project;
import com.tsl.baseapp.model.Objects.user.User;

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
