package me.sankalpchauhan.bakingtime.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.utilstesting.MessageDelayer;
import me.sankalpchauhan.bakingtime.utilstesting.SimpleIdlingResource;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.utils.Utility;
import me.sankalpchauhan.bakingtime.view.adapters.RecipeAdapter;
import me.sankalpchauhan.bakingtime.viewmodel.MainActivityViewModel;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, MessageDelayer.DelayCallback {
    private static String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.progress_circular)
    ProgressBar mProgress;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessage;
    @BindView(R.id.retry_button)
    Button mRetry;
    @BindView(R.id.recipie_rv)
    RecyclerView mRecipeRv;
    private MainActivityViewModel mainActivityViewModel;
    private List<Recipe> recipeList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    @Nullable
    private SimpleIdlingResource mIdlingResource = new SimpleIdlingResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpRecyclerView();
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finishAffinity();
                finish();
                startActivity(intent);
            }
        });
    }


    private void setUpViewModel() {
        mProgress.setVisibility(View.VISIBLE);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();

        mainActivityViewModel.getRecipieList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                assert mIdlingResource != null;
                MessageDelayer.messageProcessor("Complete", MainActivity.this, mIdlingResource);
                mProgress.setVisibility(View.INVISIBLE);
                if (recipes != null) {
                    recipeList.addAll(recipes);
                    recipeAdapter.setRecipeData(recipeList);
                    Timber.d(recipes.toString());
                    mIdlingResource.idleStae(true);
                } else {
                    mErrorMessage.setVisibility(View.VISIBLE);
                    mRetry.setVisibility(View.VISIBLE);
                    mIdlingResource.idleStae(false);
                }
            }
        });

        if (!Utility.isOnline()) {
            Toast.makeText(this, getResources().getString(R.string.cannot_connect_to_internet), Toast.LENGTH_SHORT).show();
            mErrorMessage.setText(getResources().getString(R.string.no_net_connection));
        }
    }

    private void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, isTabletOrLandscape(), RecyclerView.VERTICAL, false);
        mRecipeRv.setLayoutManager(gridLayoutManager);
        mRecipeRv.setHasFixedSize(true);
        recipeAdapter = new RecipeAdapter(this);
        mRecipeRv.setAdapter(recipeAdapter);
        setUpViewModel();

    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(Constants.RECIPE_DATA, recipe);
        startActivity(intent);
    }

    /**
     * Function actively checks if the device is a tablet or is in landscape if yes set the columns dynamically else return columns = 1
     *
     * @return
     */
    private int isTabletOrLandscape() {
        int columns = Utility.calculateNoOfColumns(this);
        boolean isTabletMode = getResources().getBoolean(R.bool.isTabletMode);
        if (isTabletMode) {
            columns = Utility.calculateNoOfColumns(this);
        } else {
            columns = 1;
        }
        return columns;
    }

    @Override
    public void complete(String text) {
        Timber.d(text);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
