package ch.epfl.sdp.musiconnect;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class LocationServiceTest {

    @Rule
    public final ServiceTestRule serviceRule =
            new ServiceTestRule();



}
