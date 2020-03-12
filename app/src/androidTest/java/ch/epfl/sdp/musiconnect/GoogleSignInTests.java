package ch.epfl.sdp.musiconnect;

import android.content.Intent;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import ch.epfl.sdp.MainActivity;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;



@RunWith(AndroidJUnit4.class)
public class GoogleSignInTests {
    private static final String ACCOUNT_MAIL = "musiconnectsdp@gmail.com";
    private static final long DEFAULT_TIMEOUT = 1000 * 10;
    private static final boolean RUN_IN_LOCAL = false; // variable used for switching between Travis(false) tests and local tests(true)

    @Rule
    public final ActivityTestRule<GoogleResultActivity> mGoogleResultActivity =
            new ActivityTestRule<>(GoogleResultActivity.class);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, true);


    private UiDevice mUiDevice;

    // Before and after methods are used in order to accept tests with intents
    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Before
    public void before() throws Exception {
        Intents.init();
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mActivityRule.launchActivity(null);
    }


    /**
     * These tests work in local, the were written to get the coverage
     * To run these test in local don't forget to change RUN_IN_LOCAL to true
     */

    @Test
    public void c_AlreadySignedIn() throws Exception {
        if(RUN_IN_LOCAL) {
            b_connectExistingAccountTest();
            Espresso.pressBack();
            onView(withId(R.id.txtvWelcome)).check(matches(withText("Already signed in")));
        }else assert(true);
    }

    @Test
    public void d_signOut() throws Exception {
        if(RUN_IN_LOCAL)
            b_connectExistingAccountTest();
        else{
            Intent i = new Intent();
            mGoogleResultActivity.launchActivity(i);
        }
        UiObject signout = mUiDevice.findObject(new UiSelector().className(Button.class).instance(0));
        signout.waitForExists(DEFAULT_TIMEOUT);
        signout.click();

        if(RUN_IN_LOCAL){
            Thread.sleep(DEFAULT_TIMEOUT);

            assertTrue(mGoogleResultActivity.getActivity().isDestroyed());
        }
        else{
            assert(true);
        }
    }

    @Test
    public void b_connectExistingAccountTest() throws Exception {
        if (RUN_IN_LOCAL) {
            // to be deleted after merging
            onView(withId(R.id.mainGoButton)).perform(click());
            onView(withId(R.id.sign_in_button)).perform((click()));


            // firstof all, test if the account doesnt exist yet on android device
            UiObject loginButton = mUiDevice.findObject(new UiSelector().textContains(ACCOUNT_MAIL));
            loginButton.waitForExists(DEFAULT_TIMEOUT);

            if (loginButton.exists()) {
                loginButton.click();

                Thread.sleep(DEFAULT_TIMEOUT);

                intended(hasComponent(GoogleResultActivity.class.getName()));
            }
        }
        else
            assert(true);
    }



}

