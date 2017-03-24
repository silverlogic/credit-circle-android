package com.tsl.baseapp.changeemail;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tsl.baseapp.R;
import com.tsl.baseapp.updatepasswordemail.UpdatePasswordAndEmailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Kevin Lavi on 9/23/16.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChangeEmailActivityTest {

    @Rule
    public ActivityTestRule<UpdatePasswordAndEmailActivity> mActivityTestRule = new ActivityTestRule<>(UpdatePasswordAndEmailActivity.class, false, false);

    @Test
    public void changeEmailActivityTest() {
        Intent intent = new Intent();
        intent.putExtra("type", "updateEmail");
        mActivityTestRule.launchActivity(intent);

        ViewInteraction currentPassword = onView(
                withId(R.id.change_email_input));
        currentPassword.perform(scrollTo(), replaceText("kl"), closeSoftKeyboard());

        ViewInteraction actionProcessButton = onView(
                allOf(withId(R.id.submit_button), withText("Update Email")));
        actionProcessButton.perform(scrollTo(), click());

        onView(withId(R.id.change_email_input)).check(matches(hasErrorText("Enter a valid email address")));
    }

}