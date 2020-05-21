package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sdp.musiconnect.testsFunctions.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

public class BandProfileTests {
    @Rule
    public IntentsTestRule<BandProfile> activityRule = new IntentsTestRule<>(BandProfile.class);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(false);
        DbSingleton.setDatabase(md);
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void setCurrentUser() {
        List<Band> ab = new ArrayList<>();
        CurrentUser.getInstance(activityRule.getActivity()).setBand(md.getBand());
        Band b = new Band("testBand", "espresso@gmail.com");
        ab.add(md.getBand());
        ab.add(b);
        CurrentUser.getInstance(activityRule.getActivity()).setBands(ab);
    }

    @Test
    public void launchActivityShouldDispalyBand() {
        onView(withId(R.id.etBandName)).perform(scrollTo()).check(matches(withText(md.getBand().getName())));
    }

    @Test
    public void changeBandSouldDisplaytestBandName() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinnerBands),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        onView(withId(R.id.etBandName)).perform(scrollTo()).check(matches(withText("testBand")));

    }
}
