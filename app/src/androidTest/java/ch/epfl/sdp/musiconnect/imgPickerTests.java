package ch.epfl.sdp.musiconnect;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class imgPickerTests {

    @Rule
    public final ActivityTestRule<UserCreation> activityRule =
            new ActivityTestRule<>(UserCreation.class);

    @Test
    public void clickOnImgView(){
        onView(withId(R.id.userProfilePicture)).perform(ViewActions.scrollTo()).perform(click());
    }
}
