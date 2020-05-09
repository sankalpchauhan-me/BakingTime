package me.sankalpchauhan.bakingtime;

import android.app.Activity;
import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.sankalpchauhan.bakingtime.view.activity.MainActivity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTests {
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule= new ActivityTestRule<>(MainActivity.class);
    private IdlingResource idlingResource;
    @Before
    public void waitBeforeDataPopulate(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>(){
            @Override
            public void perform(MainActivity activity) {
                idlingResource = activity.getIdlingResource();
                IdlingRegistry.getInstance().register(idlingResource);
            }
        });
    }
    @Test
    public void checkViewAppears() {
        //find view perform action on view
        Espresso.onView(ViewMatchers.withId(R.id.recipie_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //check that the action succeeds
        Espresso.onView(ViewMatchers.withId(R.id.fragment_steps)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterResources(){
        if(idlingResource!=null){
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
