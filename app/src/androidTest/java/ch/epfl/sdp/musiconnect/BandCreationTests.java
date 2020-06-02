package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.pages.BandCreationPage;
import ch.epfl.sdp.musiconnect.pages.StartPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class BandCreationTests {
    @Rule
    public IntentsTestRule<BandCreationPage> activityRule = new IntentsTestRule<>(BandCreationPage.class);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(false);
        DbSingleton.setDatabase(md);
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void currentUserInit() {
        CurrentUser.getInstance(activityRule.getActivity()).setTypeOfUser(TypeOfUser.Band);
    }

    @Test
    public void createBandSchouldWork() {
        onView(withId(R.id.etBandName)).perform(scrollTo(), typeText("bandTest"));
        onView(withId(R.id.btnBandCreationCreate)).perform(scrollTo(), click());
        intended(hasComponent(StartPage.class.getName()));
    }
}
