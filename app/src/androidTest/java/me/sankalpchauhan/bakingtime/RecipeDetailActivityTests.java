package me.sankalpchauhan.bakingtime;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.sankalpchauhan.bakingtime.service.model.Ingredient;
import me.sankalpchauhan.bakingtime.service.model.Recipe;
import me.sankalpchauhan.bakingtime.service.model.Step;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.view.activity.RecipeDetailActivity;
import timber.log.Timber;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static me.sankalpchauhan.bakingtime.utilstesting.TestUtils.getRandomNumber;
import static me.sankalpchauhan.bakingtime.utilstesting.TestUtils.random;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RecipeDetailActivityTests {
    private Recipe mockObject;
    private List<Step> stepList = new ArrayList<>();
    private List<Ingredient> ingredientList = new ArrayList<>();
    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<RecipeDetailActivity> mActivityRule =
            new ActivityTestRule<>(RecipeDetailActivity.class, false, false);

    @Before
    public void createMockObjects(){
        for(int i=0 ; i<=getRandomNumber(1,100); i++) {
            Step step = new Step();
            step.setId(getRandomNumber(1, 101));
            step.setDescription(random());
            step.setShortDescription(random());
            step.setThumbnailURL("");
            step.setVideoURL("");
            stepList.add(step);
        }

        for(int i=0; i<=getRandomNumber(1,100); i++){
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(random());
            ingredient.setMeasure(random());
            ingredient.setQuantity(1.0);
            ingredientList.add(ingredient);
        }

        mockObject = new Recipe();
        mockObject.setId(getRandomNumber(1,101));
        mockObject.setImage("");
        mockObject.setIngredients(ingredientList);
        mockObject.setServings(getRandomNumber(1,101));
        mockObject.setName(random());
        mockObject.setSteps(stepList);

        Timber.d("Step List Size "+stepList.size()+"Ingredient List Size"+ingredientList);
    }
    @Test
    public void ingredientListCheck() {
        //Intent Stub
        Intent i = new Intent();
        i.putExtra(Constants.RECIPE_DATA, mockObject);
        mActivityRule.launchActivity(i);
        //find view perform action on view
        Espresso.onView(ViewMatchers.withId(R.id.ingredient_btn)).perform(click());
        //check
        Espresso.onView(ViewMatchers.withId(R.id.ingredient_rv)).check(matches(isDisplayed()));
    }
    @Test
    public void ingredientNextViewAppears() {
        Intent i = new Intent();
        i.putExtra(Constants.RECIPE_DATA, mockObject);
        mActivityRule.launchActivity(i);
        //find view perform action on view
        Espresso.onView(ViewMatchers.withId(R.id.steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //check
        Espresso.onView(ViewMatchers.withId(R.id.step_frag)).check(matches(isDisplayed()));
    }


}
