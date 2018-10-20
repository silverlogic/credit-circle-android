package com.tsl.money2020.changepassword;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tsl.money2020.R;
import com.tsl.money2020.updatepasswordemail.UpdatePasswordAndEmailActivity;

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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChangePasswordActivityTest {

    @Rule
    public ActivityTestRule<UpdatePasswordAndEmailActivity> mActivityTestRule = new ActivityTestRule<>(UpdatePasswordAndEmailActivity.class, false, false);

    @Test
    public void signUpActivityTest() {
        Intent intent = new Intent();
        intent.putExtra("type", "changePassword");
        mActivityTestRule.launchActivity(intent);

        ViewInteraction currentPassword = onView(
                withId(R.id.current_password));
        currentPassword.perform(scrollTo(), replaceText("12345"), closeSoftKeyboard());

        ViewInteraction newPassword = onView(
                withId(R.id.new_password));
        newPassword.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction confirmNewPassword = onView(
                withId(R.id.confirm_new_password));
        confirmNewPassword.perform(scrollTo(), replaceText("12"), closeSoftKeyboard());

        ViewInteraction actionProcessButton = onView(
                allOf(withId(R.id.submit_button), withText("Change Password")));
        actionProcessButton.perform(scrollTo(), click());

        //onView(withId(R.id.new_password)).check(matches(hasErrorText("Field cannot be empty")));
        //onView(withId(R.id.confirm_new_password)).check(matches(hasErrorText("Field cannot be empty")));
        onView(withId(R.id.confirm_new_password)).check(matches(hasErrorText("New passwords must match")));
    }

}