package com.example.assignmenttracker;

import android.app.Application;

public class UserRoleSelection extends Application  {
    private String userRoleVariable;

    public String getUserRoleVariable() {
        return userRoleVariable;
    }

    public void setUserRoleVariable(String userRoleVariable) {
        this.userRoleVariable = userRoleVariable;
    }
}
