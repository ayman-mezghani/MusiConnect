package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.database.MockDatabase;
import ch.epfl.sdp.musiconnect.pages.UserCreationPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class imgPickerTests {

    @Rule
    public final ActivityTestRule<UserCreationPage> activityRule =
            new ActivityTestRule<>(UserCreationPage.class);

    @BeforeClass
    public static void setMocks() {
        DbSingleton.setDatabase(new MockDatabase(false));
        CloudStorageSingleton.setStorage((new MockCloudStorage()));
    }

    @Test
    public void clickOnImgView(){
        onView(withId(R.id.userProfilePicture)).perform(ViewActions.scrollTo()).perform(click());
    }
}
