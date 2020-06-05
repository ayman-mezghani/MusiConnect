package ch.epfl.sdp.musiconnect;


import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.functionnalities.MyDate;
import ch.epfl.sdp.musiconnect.pages.VisitorProfilePage;
import ch.epfl.sdp.musiconnect.users.Musician;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@LargeTest
//@RunWith(AndroidJUnit4.class)
public class BandAddUsersTests {

    @Rule
    public ActivityTestRule<VisitorProfilePage> mActivityTestRule = new ActivityTestRule<>(VisitorProfilePage.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase(true);
        DbSingleton.setDatabase(md);
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Before
    public void setCurrentUser() {
        CurrentUser.getInstance(mActivityTestRule.getActivity()).setTypeOfUser(UserType.Band);
        CurrentUser.getInstance(mActivityTestRule.getActivity()).setBand(md.getBand());
    }

    @Test
    public void addUserToBandTest() {
        Musician m = new Musician("Alice", "Bardon", "Alyx", "aymanmezghani97@gmail.com", new MyDate(1992, 9, 20));
        m.setUserType(UserType.Band);
        Intent intent = new Intent();
        intent.putExtra("UserEmail", m.getEmailAddress());
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.add_user_to_band)).perform(scrollTo(), click());
        assertTrue(md.getBand().containsMember(m.getEmailAddress()));

        mActivityTestRule.finishActivity();
    }
}

