package me.sankalpchauhan.bakingtime.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.service.model.Step;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.view.fragments.StepDetailFragment;
import timber.log.Timber;

public class StepDetailActivity extends AppCompatActivity {
    @BindView(R.id.nextBtn)
    Button mNext;
    @BindView(R.id.previousBtn)
    Button mPrevious;
    private Step mStep;
    private int mStepPosition;
    private List<Step> stepList = new ArrayList<>();
    private ActionBar ab;
    private Fragment stepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent previousData = getIntent();
        if (previousData != null) {
            if (previousData.hasExtra(Constants.STEP_DATA)) {
                //Just Noticed: Particular Step data was not required position & list of steps/recipe was alone sufficient, will fix later
                mStep = (Step) previousData.getSerializableExtra(Constants.STEP_DATA);
                Timber.d("Check: Getting Data" + (mStep != null ? mStep.getDescription() : null));
            }
            if (previousData.hasExtra(Constants.STEP_POSITION)) {
                mStepPosition = (int) previousData.getIntExtra(Constants.STEP_POSITION, 0);
                Timber.d("Check: Getting Data" + mStepPosition);
            }
            if (previousData.hasExtra(Constants.RECIPE_DATA)) {
                Recipe mRecipe = (Recipe) previousData.getSerializableExtra(Constants.RECIPE_DATA);
                stepList = mRecipe.getSteps();
                Timber.d("Check: Getting Data" + (stepList != null ? stepList.size() : null));
            }
        }
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        ab = getSupportActionBar();
        setAppropriateTitle("Step " + mStepPosition + ": " + mStep.getShortDescription());
        if (savedInstanceState != null) {
            stepDetailFragment = getSupportFragmentManager().getFragment(savedInstanceState, Constants.STEP_FRAGMENT_NAME);
            setAppropriateTitle(savedInstanceState.getCharSequence(Constants.ACTION_BAR_RESTORE).toString());
            mStepPosition = savedInstanceState.getInt(Constants.CURRENT_STEP_POSITION);
            aptUIUpdate();
        } else {
            stepDetailFragment = new StepDetailFragment();
        }
        Bundle throwBundle = new Bundle();
        throwBundle.putSerializable(Constants.STEP_DATA, stepList.get(mStepPosition));
        stepDetailFragment.setArguments(throwBundle);
        aptUIUpdate();
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepPosition < stepList.size() - 1) {
                    mStepPosition++;
                    setAppropriateTitle("Step " + mStepPosition + ": " + stepList.get(mStepPosition).getShortDescription());
                    //Replacing Fragment With New Step
                    newFragmentManagaerCommit();
                } else {
                    onBackPressed();
                    Toast.makeText(StepDetailActivity.this, getResources().getString(R.string.recipe_complete), Toast.LENGTH_SHORT).show();
                }
                aptUIUpdate();
                Timber.d("StepChange: " + mStepPosition);
            }
        });
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepPosition > 0) {
                    mStepPosition--;
                    setAppropriateTitle("Step " + mStepPosition + ": " + stepList.get(mStepPosition).getShortDescription());
                    //Replacing Fragment With New Step
                    newFragmentManagaerCommit();
                }
                aptUIUpdate();
                Timber.d("StepChange: " + mStepPosition);
            }
        });
        fragmentManagerCommit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setAppropriateTitle(String s) {
        if (ab != null) {
            ab.setTitle(s);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.CURRENT_CURSOR_CONSTANT, mStepPosition);
        setResult(RESULT_OK, intent);
        Timber.d("STEP UPDATE" + mStepPosition);
        super.onBackPressed();
    }

    public void aptUIUpdate() {
        if (mStepPosition == stepList.size() - 1) {
            //mNext.setVisibility(View.GONE);
            mNext.setText(getResources().getString(R.string.finish_btn));
        } else {
            mNext.setVisibility(View.VISIBLE);
            mNext.setText(getResources().getString(R.string.next));
        }
        if (mStepPosition == 0) {
            mPrevious.setVisibility(View.GONE);
        } else {
            mPrevious.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, Constants.STEP_FRAGMENT_NAME, stepDetailFragment);
        outState.putInt(Constants.CURRENT_STEP_POSITION, mStepPosition);
        outState.putCharSequence(Constants.ACTION_BAR_RESTORE, ab.getTitle());
    }

    public void fragmentManagerCommit() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_frag, stepDetailFragment)
                .commit();
    }

    public void newFragmentManagaerCommit() {
        Bundle throwBundle = new Bundle();
        throwBundle.putSerializable(Constants.STEP_DATA, stepList.get(mStepPosition));
        stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(throwBundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.step_frag, stepDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen and make changes accordingly
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if ((!mStep.getVideoURL().isEmpty() || !mStep.getThumbnailURL().isEmpty())) {
                getSupportActionBar().hide();
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mNext.setVisibility(View.INVISIBLE);
                mPrevious.setVisibility(View.INVISIBLE);
            }
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().show();
            mNext.setVisibility(View.VISIBLE);
            mPrevious.setVisibility(View.VISIBLE);
        }
    }

}
