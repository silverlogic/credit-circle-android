package com.tsl.money2020.signup;


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
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void signUpActivityTest() {

        ViewInteraction firstNameEditText = onView(
                withId(R.id.input_first_name));
        firstNameEditText.perform(scrollTo(), replaceText("kevin"), closeSoftKeyboard());

        ViewInteraction lastNameEditText = onView(
                withId(R.id.input_last_name));
        lastNameEditText.perform(scrollTo(), replaceText("Lavi"), closeSoftKeyboard());

        ViewInteraction emailEditText = onView(
                withId(R.id.input_email));
        emailEditText.perform(scrollTo(), replaceText("kl@tsl.io"), closeSoftKeyboard());

        ViewInteraction passEditText = onView(
                withId(R.id.input_password));
        passEditText.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction confirmPassEditText = onView(
                withId(R.id.input_password_confirm));
        confirmPassEditText.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction actionProcessButton = onView(
                allOf(withId(R.id.btn_signup), withText("Sign Up")));
        actionProcessButton.perform(scrollTo(), click());

//        onView(withId(R.id.input_email)).check(matches(hasErrorText("Enter a valid email address")));
//        onView(withId(R.id.input_password_confirm)).check(matches(hasErrorText("Passwords must match")));
    }

}
