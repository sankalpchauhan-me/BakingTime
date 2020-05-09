package me.sankalpchauhan.bakingtime.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.service.model.Step;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.view.activity.RecipeDetailActivity;
import me.sankalpchauhan.bakingtime.view.adapters.StepsAdapter;
import timber.log.Timber;

public class RecipeStepsFragment extends Fragment implements StepsAdapter.StepAdapterOnClickHandler {
    @BindView(R.id.steps_rv)
    RecyclerView mStepsRv;
    StepAdapterOnClickHandlerFragment mCallback;
    private List<Step> stepList = new ArrayList<>();
    private Recipe mRecipe;
    private StepsAdapter stepsAdapter;
    private Bundle bundleSave;

    public RecipeStepsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipie_steps_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Timber.d("I am here 1");
        if (mRecipe == null) {
            Timber.d("I am here 2");
            if (getActivity() instanceof RecipeDetailActivity) {
                Timber.d("I am here 3");
                mRecipe = ((RecipeDetailActivity) getActivity()).getRecipe();
                stepList = mRecipe.getSteps();
                Timber.d("Check" + mRecipe.getName());
            }
            Timber.d("Check" + stepList.toString());
        }
        setUpRecyclerView();
        bundleSave = savedInstanceState;
        if (bundleSave != null) {
            stepsAdapter.selected_position = bundleSave.getInt(Constants.CURRENT_STEP_POSITION);
            stepsAdapter.notifyDataSetChanged();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleSave == null && getActivity() instanceof RecipeDetailActivity) {
            stepsAdapter.selected_position = ((RecipeDetailActivity) getActivity()).getCursorPosition();
            stepsAdapter.notifyDataSetChanged();
        }
        Timber.d("Selected Position: " + stepsAdapter.selected_position);
        bundleSave = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (StepAdapterOnClickHandlerFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " StepAdapterOnClickHandlerFragment");
        }
    }

    private void setUpRecyclerView() {
        Timber.d("I am here 4");
        mStepsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStepsRv.setHasFixedSize(true);
        stepsAdapter = new StepsAdapter(this);
        mStepsRv.setAdapter(stepsAdapter);
        stepsAdapter.setStepsData(stepList);
    }

    @Override
    public void onClick(Step step, int position) {
        mCallback.onStepSelected(step, position);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.CURRENT_STEP_POSITION, stepsAdapter.selected_position);
    }

    public interface StepAdapterOnClickHandlerFragment {
        void onStepSelected(Step step, int position);
    }
}
