package com.luka.mackovic.eus.ui.activity;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.luka.mackovic.eus.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
public class HomeActivityTest {

    @Rule public ActivityTestRule<HomeActivity_> activityTestRule
            = new ActivityTestRule<>(HomeActivity_.class);

    @Test
    public void test_isActivityInView() {
        onView(withId(R.id.nav_events)).check(matches(isDisplayed()));
    }
}