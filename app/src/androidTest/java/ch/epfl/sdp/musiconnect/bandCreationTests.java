package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class bandCreationTests {
    @Rule
    public IntentsTestRule<BandCreation> activityRule = new IntentsTestRule<>(BandCreation.class);

    @BeforeClass
    public static void setMocks() {
        DbGenerator.setDatabase(new MockDatabase(false));
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }

    @Before
    public void currentUserInit() {
        CurrentUser.getInstance(activityRule.getActivity()).setTypeOfUser(TypeOfUser.Band);
    }

    @Test
    public void createBandSchouldWork() {
        onView(withId(R.id.etBandName)).perform(scrollTo(), typeText("bandTest"));
        onView(withId(R.id.btnBandCreationCreate)).perform(scrollTo(), click());
    }
}
