package com.tsl.money2020.forgotpassword;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tsl.money2020.R;

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
public class ForgotPasswordActivityTest {

    @Rule
    public ActivityTestRule<ForgotPasswordActivity> mActivityTestRule = new ActivityTestRule<>(ForgotPasswordActivity.class);

    @Test
    public void forgotPasswordActivityTest() {

        ViewInteraction emailEditText = onView(
                withId(R.id.input_email));
        emailEditText.perform(scrollTo(), replaceText("kl"), closeSoftKeyboard());

        ViewInteraction actionProcessButton = onView(
                allOf(withId(R.id.forgot_password_button), withText("Submit")));
        actionProcessButton.perform(scrollTo(), click());

        onView(withId(R.id.input_email)).check(matches(hasErrorText("Enter a valid email address")));
    }

}