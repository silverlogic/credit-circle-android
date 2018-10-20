package com.tsl.money2020.feed;


import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.orhanobut.hawk.Hawk;
import com.tsl.money2020.R;
import com.tsl.money2020.api.RestServiceTestHelper;
import com.tsl.money2020.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FeedActivityTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<FeedActivity> mActivityTestRule = new ActivityTestRule<>(FeedActivity.class, true, false);

    private MockWebServer mMockWebServer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Hawk.put(Constants.TOKEN, "123456");
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Constants.STAGING_URL = mMockWebServer.url("/").toString();
    }

    @Test
    public void feedActivityTest() throws Exception {
        String fileName = "user_list.json";
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        // checks if profile, settings and search is displayed.
        // Performs click on settings since we don't need to mock api for that.
        // Performs click on feed item

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_settings), withContentDescription("Settings"), isDisplayed()));
        actionMenuItemView.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        onView(allOf(withId(R.id.action_profile), withContentDescription("Settings"), isDisplayed()));


        onView(allOf(withId(R.id.action_search), withContentDescription("Search…"), isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.feed),
                        withParent(withId(R.id.swipe)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        pressBack();

    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
        Hawk.remove(Constants.TOKEN);
    }

}
