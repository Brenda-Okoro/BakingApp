package com.example.brenda.bakingapp;

import android.support.test.espresso.IdlingResource;

import com.example.brenda.bakingapp.activities.RecipeListActivity;

/**
 * Created by brenda on 10/24/17.
 */

public class BakingIdlingResource implements IdlingResource
{
    private RecipeListActivity activity;
    private IdlingResource.ResourceCallback callback;

    public BakingIdlingResource(RecipeListActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return BakingIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        return activity != null && callback != null && activity.isSyncFinished();
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}

