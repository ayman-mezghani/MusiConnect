package ch.epfl.sdp.musiconnect;


import android.location.Location;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import ch.epfl.sdp.R;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class UserLocationActivityTest{



    /**
     * Helper method to avoid duplicate code
     * @param stringId
     */
    private void openActionsMenu(int stringId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(stringId)).perform(click());
    }

    @Rule
    public final ActivityTestRule<MapsActivity> uActivityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @Test
    public void testSettingFakeLocationReturnsRightLocation() {
        openActionsMenu(R.string.map);


        Location location = new Location("Test");
        location.setLatitude(10.0);
        location.setLongitude(30.0);
        location.setTime(System.currentTimeMillis());


        uActivityRule.getActivity().setLocation(location);
        Location l = uActivityRule.getActivity().getLocation();
        assertThat(l.getLatitude(), is(location.getLatitude()));
    }

}