package com.example.brenda.bakingapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.brenda.bakingapp.R;
import com.example.brenda.bakingapp.fragments.RecipeStepsFragment;
import com.example.brenda.bakingapp.models.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brenda on 10/23/17.
 */

public class RecipeStepsFragmentAdapter extends FragmentPagerAdapter {
    private List<Steps> mSteps = new ArrayList<>();
    Context mContext;

    public RecipeStepsFragmentAdapter(Context context, FragmentManager manager, List<Steps> steps) {
        super(manager);
        mSteps = steps;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        return  RecipeStepsFragment.getNewInstance(mSteps.get(position));
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String step =  mContext.getString(R.string.step);
        return  step + " " + position;
    }
}

