package com.tsl.money2020.userdetails;

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
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.model.objects.user.UserImages;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.Writer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tsl.money2020.utils.ExtraAssertions.isGone;
import static com.tsl.money2020.utils.ExtraAssertions.isVisible;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserDetailsActivityTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<UserDetailsActivity> mActivityTestRule = new ActivityTestRule<>(UserDetailsActivity.class, true, false);

    private MockWebServer mMockWebServer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        User user = new User();
        user.setId(117);
        user.setFirst_name("Kevin");
        user.setLast_name("Lavi");
        UserImages images = new UserImages();
        images.setFull_size("https://baseapp-api-staging.s3.amazonaws.com:443/media/user-avatars/kl%2B1%40tsl.io/resized/1024/b81adb95-c07.png");
        images.setSmall("https://baseapp-api-staging.s3.amazonaws.com:443/media/user-avatars/kl%2B1%40tsl.io/resized/64/b81adb95-c07.png");
        user.setUserImages(images);
        Hawk.put(Constants.USER_ID, 117);
        User persistUser = Writer.persist(user);
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Constants.STAGING_URL = mMockWebServer.url("/").toString();
    }

    @Test
    public void currentUserDetailsActivityTest() throws Exception {
        // from profile fetch for current user, names are editable, button is visable
        String fileName = "user_detail.json";
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        intent.putExtra(UserDetailsFragment.USER_ID, 117);
        intent.putExtra(UserDetailsFragment.IS_CURRENT_USER, true);
        mActivityTestRule.launchActivity(intent);


        ViewInteraction appCompatEditText = onView(
                withId(R.id.edit_first_name));
        appCompatEditText.perform(scrollTo(), replaceText("Kevin"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.edit_last_name));
        appCompatEditText2.perform(scrollTo(), replaceText("Lavi"), closeSoftKeyboard());

        onView(withId(R.id.confirm_changes_button)).check(isVisible());
    }

    @Test
    public void feedUserDetailsActivityTest() throws Exception {
        // from feed user is not editable and confirm button is gone
        Intent intent = new Intent();
        intent.putExtra(UserDetailsFragment.USER_ID, 117);
        intent.putExtra(UserDetailsFragment.IS_CURRENT_USER, false);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.edit_first_name)).check(matches(not(isEnabled())));

        onView(withId(R.id.edit_last_name)).check(matches(not(isEnabled())));

        onView(withId(R.id.confirm_changes_button)).check(isGone());
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
        Hawk.remove(Constants.USER_ID);
    }

}
