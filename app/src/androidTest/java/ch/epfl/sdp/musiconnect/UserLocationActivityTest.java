package ch.epfl.sdp.musiconnect;


import android.location.Location;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class UserLocationActivityTest{

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Rule
    public final ActivityTestRule<UserLocationActivity> uActivityRule =
            new ActivityTestRule<>(UserLocationActivity.class);

    @Test
    public void testSettingFakeLocationReturnsRightLocation() {
         onView(withId(R.id.mainGoButton)).perform(click());


        Location location = new Location("Test");
        location.setLatitude(10.0);
        location.setLongitude(30.0);
        location.setTime(System.currentTimeMillis());


        uActivityRule.getActivity().setLocation(location);
        Location l = uActivityRule.getActivity().getLocation();
        assertThat(l.getLatitude(), is(location.getLatitude()));
    }

}