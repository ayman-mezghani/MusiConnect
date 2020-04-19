package ch.epfl.sdp.musiconnect;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventTests {
    @Rule
    public final ActivityTestRule<EventPage> profilePageRule =
            new ActivityTestRule<>(EventPage.class);




}
