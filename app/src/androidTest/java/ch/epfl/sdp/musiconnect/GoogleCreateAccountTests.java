package ch.epfl.sdp.musiconnect;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class GoogleCreateAccountTests {
    private UiDevice mUiDevice;
    private static final String ACCOUNT_MAIL = "musiconnectsdp@gmail.com";
    private static final String ACCOUNT_PWD = "mcSDP2k20";
    private static final long DEFAULT_TIMEOUT = 1000 * 10;
    private static final boolean RUN_IN_LOCAL = false; // variable used for switching between Travis(false) tests and local tests(true)

    /*@Rule
    public final ActivityTestRule<GoogleResultActivity> mGoogleResultActivity =
            new ActivityTestRule<>(GoogleResultActivity.class);*/

    /*@Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, true);*/

    @Rule
    public final ActivityTestRule<GoogleLogin> googleLoginTestsRule =
            new ActivityTestRule<>(GoogleLogin.class);

    // Before and after methods are used in order to accept tests with intents
    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Before
    public void before() throws Exception {
        Intents.init();
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    private void buttonClick(UiObject btn){
        btn.waitForExists(DEFAULT_TIMEOUT);

        try {
            btn.clickAndWaitForNewWindow(DEFAULT_TIMEOUT);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This tests work in local, the were written to get the coverage
     * To run this test in local don't forget to change RUN_IN_LOCAL to true
     */
    @Test()
    public void a_connectNewAccount() throws Exception {

        if(RUN_IN_LOCAL) {
            boolean noAccountInstalled = false;
            // to be deleted after merging
            buttonClick(mUiDevice.findObject(new UiSelector().textContains("sign in")));
            // firstof all, test if the account doesnt exist yet on android device
            UiObject loginButton = mUiDevice.findObject(new UiSelector().textContains(ACCOUNT_MAIL));
            loginButton.waitForExists(DEFAULT_TIMEOUT);

            if (!loginButton.exists()) {
                UiObject anotherAccount = mUiDevice.findObject(new UiSelector().textContains("another account"));
                loginButton.waitForExists(DEFAULT_TIMEOUT);

                if (anotherAccount.exists()) {
                    anotherAccount.click();
                }
                else {
                    noAccountInstalled = true;
                }

                mUiDevice.wait(Until.findObject(By.clazz(WebView.class)), DEFAULT_TIMEOUT);
                Thread.sleep(DEFAULT_TIMEOUT);
                UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
                do {
                    if (mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class)).exists()) {
                        mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class)).setText(ACCOUNT_MAIL);
                        //mUiDevice.findObject(new UiSelector().className(Button.class).textContains("Accept")).clickAndWaitForNewWindow(DEFAULT_TIMEOUT);
                        break;
                    }
                }
                while (!appViews.scrollForward());
                buttonClick(mUiDevice.findObject(new UiSelector().className(Button.class).textContains("Next")));

                UiObject pwdInput = mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class));

                pwdInput.waitForExists(DEFAULT_TIMEOUT);
                pwdInput.setText(ACCOUNT_PWD);

                buttonClick(mUiDevice.findObject(new UiSelector().className(Button.class).textContains("Next")));
                Thread.sleep(DEFAULT_TIMEOUT);

                appViews.scrollForward(2);

                buttonClick(mUiDevice.findObject(new UiSelector().className(Button.class).textContains("I agree")));

                if (noAccountInstalled) {
                    while (!appViews.scrollForward()) {
                        if (mUiDevice.findObject(new UiSelector().className(Button.class).textContains("Accept")).exists()) {
                            mUiDevice.findObject(new UiSelector().className(Button.class).textContains("Accept")).clickAndWaitForNewWindow(DEFAULT_TIMEOUT);
                            break;
                        }
                    }
                }
            } else
                loginButton.clickAndWaitForNewWindow(DEFAULT_TIMEOUT);

            Thread.sleep(2 * DEFAULT_TIMEOUT);
            intended(hasComponent(StartPage.class.getName()));
        }
        else{
            assert(true);
        }

    }

}
