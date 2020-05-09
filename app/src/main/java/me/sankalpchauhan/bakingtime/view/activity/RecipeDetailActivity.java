package me.sankalpchauhan.bakingtime.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.service.model.Step;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.view.fragments.IngredientBottomSheet;
import me.sankalpchauhan.bakingtime.view.fragments.RecipeStepsFragment;
import me.sankalpchauhan.bakingtime.view.fragments.StepDetailFragment;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.StepAdapterOnClickHandlerFragment {
    private Recipe recipe;
    private boolean mTwoPane = false;
    private int cursorPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent previousIntent = getIntent();
        if (previousIntent != null) {
            if (previousIntent.hasExtra(Constants.RECIPE_DATA)) {
                recipe = (Recipe) previousIntent.getSerializableExtra(Constants.RECIPE_DATA);
            }
        }
        setContentView(R.layout.activity_recipe_detail);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(recipe.getName() + " Recipe");
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.tabletMode_ll) != null) {
            mTwoPane = true;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.ingredient_btn:
                Bundle b = new Bundle();
                b.putSerializable(Constants.RECIPE_DATA, recipe);
                IngredientBottomSheet ingredientBottomSheet = new IngredientBottomSheet();
                ingredientBottomSheet.setArguments(b);
                ingredientBottomSheet.show(getSupportFragmentManager(), Constants.INGREDIENT_BOTTOM_SHEET_TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Recipe getRecipe() {
        return recipe;
    }


    @Override
    public void onStepSelected(Step step, int position) {
        if (mTwoPane) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.STEP_DATA, step);
            stepDetailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_step_detail_frag, stepDetailFragment)
                    .commit();

        } else {
            Intent i = new Intent(this, StepDetailActivity.class);
            i.putExtra(Constants.STEP_DATA, step);
            i.putExtra(Constants.RECIPE_DATA, recipe);
            i.putExtra(Constants.STEP_POSITION, position);
            startActivityForResult(i, Constants.POSITION_UPDATE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.POSITION_UPDATE_CODE) {
            if (resultCode == RESULT_OK) {
                cursorPosition = data.getIntExtra(Constants.CURRENT_CURSOR_CONSTANT, -1);
            }
        }
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

}
