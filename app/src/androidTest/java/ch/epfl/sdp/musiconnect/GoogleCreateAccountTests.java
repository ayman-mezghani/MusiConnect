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
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.MainActivity;
import ch.epfl.sdp.R;

import static android.provider.Settings.System.getString;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class GoogleCreateAccountTests {
    private UiDevice mUiDevice;
    private static final String ACCOUNT_MAIL = "musiconnectsdp@gmail.com";
    private static final String ACCOUNT_PWD = "mcSDP2k20";
    private static final long DEFAULT_TIMEOUT = 1000 * 5;

    @Rule
    public final ActivityTestRule<GoogleResultActivity> mGoogleResultActivity =
            new ActivityTestRule<>(GoogleResultActivity.class);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, true);

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
            btn.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test()
    public void a_connectNewAccount() throws Exception {
        boolean noAccountInstalled = false;
        // to be deleted after merging
        onView(withId(R.id.mainGoButton)).perform(click());
        buttonClick(mUiDevice.findObject(new UiSelector().textContains("sign in")));

        // firstof all, test if the account doesnt exist yet on android device
        UiObject loginButton = mUiDevice.findObject(new UiSelector().textContains(ACCOUNT_MAIL));
        loginButton.waitForExists(DEFAULT_TIMEOUT);

        if (!loginButton.exists()) {
            UiObject anotherAccount = mUiDevice.findObject(new UiSelector().textContains("another account"));
            loginButton.waitForExists(DEFAULT_TIMEOUT);

            if(anotherAccount.exists())
                anotherAccount.click();
            else
                noAccountInstalled = true;

            mUiDevice.wait(Until.findObject(By.clazz(WebView.class)), DEFAULT_TIMEOUT);

            UiObject emailInput = mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class));

            emailInput.waitForExists(DEFAULT_TIMEOUT);
            emailInput.setText(ACCOUNT_MAIL);

            buttonClick(mUiDevice.findObject(new UiSelector().instance(2).className(Button.class)));
            Thread.sleep(DEFAULT_TIMEOUT);

            UiObject pwdInput = mUiDevice.findObject(new UiSelector().instance(0).className(EditText.class));

            pwdInput.waitForExists(DEFAULT_TIMEOUT);
            pwdInput.setText(ACCOUNT_PWD);

            buttonClick(mUiDevice.findObject(new UiSelector().instance(1).className(Button.class)));
            Thread.sleep(DEFAULT_TIMEOUT);

            buttonClick(mUiDevice.findObject(new UiSelector().instance(0).className(Button.class)));
            Thread.sleep(DEFAULT_TIMEOUT);

            if(noAccountInstalled){
                buttonClick(mUiDevice.findObject(new UiSelector().className(Button.class).textContains("Accept")));
                Thread.sleep(DEFAULT_TIMEOUT);
            }
        }
        else
            loginButton.click();

        Thread.sleep(DEFAULT_TIMEOUT);
        intended(hasComponent(GoogleResultActivity.class.getName()));
    }

}
