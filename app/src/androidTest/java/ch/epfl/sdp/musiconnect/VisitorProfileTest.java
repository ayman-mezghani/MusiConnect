package ch.epfl.sdp.musiconnect;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class VisitorProfileTest {
    private UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public final ActivityTestRule<VisitorProfilePage> visitorActivityTestRule = new ActivityTestRule<>(VisitorProfilePage.class,
            true, false);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase());
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }

    @Test
    public void testNoMarkerTransitionToProfile() {
        Musician m1 = new Musician("Alice", "Bardon", "Alyx", "aymanmezghani97@gmail.com", new MyDate(1992, 9, 20));
        Musician m2 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));

        testMusician(m1);
        testMusician(m2);
    }

    private void testMusician(Musician m) {
        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        visitorActivityTestRule.launchActivity(intent);


        onView(withId(R.id.visitorProfileFirstname)).check(matches(withText(m.getFirstName())));
        onView(withId(R.id.visitorProfileLastname)).check(matches(withText(m.getLastName())));
        onView(withId(R.id.visitorProfileUsername)).check(matches(withText(m.getUserName())));
        onView(withId(R.id.visitorProfileEmail)).check(matches(withText(m.getEmailAddress())));
        onView(withId(R.id.visitorProfileBirthday)).check(matches(withText(m.getBirthday().toString())));

        visitorActivityTestRule.finishActivity();
    }

    @Test
    public void loadNoProfile() {
        Intent intent = new Intent();
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileTitle)).check(matches(withText("Profile not found...")));
    }

    @Test
    public void loadNullProfile() {
        Intent intent = new Intent();
        intent.putExtra("Username", (String) null);
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileTitle)).check(matches(withText("Profile not found...")));
    }

    @Test
    public void testNonExistentProfile() {
        Intent intent = new Intent();
        intent.putExtra("Username", "NotAUsername");
        visitorActivityTestRule.launchActivity(intent);

        onView(withId(R.id.visitorProfileTitle)).check(matches(withText("Profile not found...")));
    }

    @Test
    public void testChangeTextOfContactButton() {
        Musician m = new Musician(
                "Bob",
                "Mallet",
                "Bob",
                "bob@gmail.com",
                new MyDate(2000, 1, 1)
        );

        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        visitorActivityTestRule.launchActivity(intent);

        onView(allOf(withText("Contact" + m.getFirstName()), withParent(withId(R.id.btnContactMusician))));

        visitorActivityTestRule.finishActivity();
    }

    @Test
    public void testContactButtonClickShouldDisplayMessage() {
        Musician m = new Musician(
                "Julien",
                "Dore",
                "ilestpasbeau",
                "jdore@gmail.com",
                new MyDate(1950, 12, 31)
        );

        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        visitorActivityTestRule.launchActivity(intent);
        onView(withId(R.id.btnContactMusician)).perform(scrollTo(), click());

        device.wait(Until.hasObject(By.textStartsWith("Gmail")), 600);
        device.findObject(By.textStartsWith("Gmail")).click();

        device.wait(Until.hasObject(By.textStartsWith("Compose")), 600);
        device.pressBack();
    }
}
