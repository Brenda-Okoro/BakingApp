package com.example.brenda.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brenda.bakingapp.R;
import com.example.brenda.bakingapp.models.Ingredients;
import com.example.brenda.bakingapp.models.Steps;
import com.example.brenda.bakingapp.utils.ImageUtils;

import java.util.List;

/**
 * Created by brenda on 10/23/17.
 */

public class RecipeListDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Steps> steps;
    private List<Ingredients> ingredients;
    private Context applicationContext;
    private RecipeStepItemTouchListener listener;

    private static final int INGREDIENT_POSITION = 0;

    public interface RecipeStepItemTouchListener {
        void onRecipeStepItemTouched(int position);
    }


    public RecipeListDetailsAdapter(Context context, List<Ingredients> ingredients, List<Steps> steps) {
        this.steps = steps;
        applicationContext = context;
        this.ingredients = ingredients;

    }

    public void setRecipeStepOnTouchListener(RecipeStepItemTouchListener recipeStepOnTouchListener) {
        listener = recipeStepOnTouchListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == INGREDIENT_POSITION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_list, parent, false);
            return new IngredientViewHolder(view, applicationContext);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_list_item, parent, false);
            return new StepViewHolder(applicationContext, view, listener);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == INGREDIENT_POSITION) {
            ((IngredientViewHolder) holder).bindView(ingredients);
        } else {
            int stepPosition = position - 1;
            Steps currentStep = steps.get(stepPosition);
            boolean isLastPosition = (stepPosition == steps.size() - 1);
            Log.e("dd", isLastPosition + "");
            ((StepViewHolder) holder).bindView(currentStep, stepPosition, isLastPosition);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size() + 1;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView mShortDescription;
        Context context;
        RecipeStepItemTouchListener mTouchListener;
        View mItemView;
        View mDivider;
        ImageView mPlayIcon;

        public StepViewHolder(Context context, View itemView, RecipeStepItemTouchListener listener) {
            super(itemView);
            this.context = context;
            mTouchListener = listener;
            mItemView = itemView;
            mShortDescription = (TextView) itemView.findViewById(R.id.tv_recipe_step_short_description);
            mDivider = itemView.findViewById(R.id.v_divider);
            mPlayIcon = (ImageView) itemView.findViewById(R.id.iv_play_icon);


        }

        public void bindView(final Steps step, final int position) {
            String stepDescription = position + ".  " + step.getShortDescription();
            mShortDescription.setText(stepDescription);

            if (step.hasVideo()) {
                mPlayIcon.setVisibility(View.VISIBLE);
                ImageUtils.loadImageFromResourcesToImageView(context, R.drawable.youtube, mPlayIcon);
            }

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTouchListener != null)
                        mTouchListener.onRecipeStepItemTouched(position);
                }
            });

        }

        public void bindView(final Steps step, int position, boolean isLastItem) {
            bindView(step, position);

            if (isLastItem) {
                mDivider.setVisibility(View.GONE);
            }
        }


    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView mIngredients;
        private Context mApplicationContext;

        public IngredientViewHolder(View itemView, Context applicationContext) {
            super(itemView);
            mIngredients = (TextView) itemView.findViewById(R.id.tv_ingredients);
            mApplicationContext = applicationContext;
        }

        public void bindView(List<Ingredients> ingredients) {
            String formattedIngredient;
            int quantity;
            String measure;
            String ingredientDetails;

            for (Ingredients ingredient : ingredients) {
                formattedIngredient = mApplicationContext.getString(R.string.bullet);
                quantity = ingredient.getQuantity();
                measure = ingredient.getMeasure();
                ingredientDetails = ingredient.getIngredient();
                formattedIngredient += " " + ingredientDetails + " (" + quantity + " " + measure + ")";
                mIngredients.append(formattedIngredient + "\n\n");

            }
        }
    }


}
