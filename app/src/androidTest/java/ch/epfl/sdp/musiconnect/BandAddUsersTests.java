//package ch.epfl.sdp.musiconnect;
//
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//
//import androidx.test.espresso.ViewInteraction;
//import androidx.test.filters.LargeTest;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.rule.GrantPermissionRule;
//import androidx.test.runner.AndroidJUnit4;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import ch.epfl.sdp.R;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.scrollTo;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static ch.epfl.sdp.musiconnect.testsFunctions.*;
//import static org.hamcrest.Matchers.allOf;
//import static org.hamcrest.Matchers.is;
//
//@LargeTest
////@RunWith(AndroidJUnit4.class)
//public class BandAddUsersTests {
//
//    @Rule
//    public ActivityTestRule<StartPage> mActivityTestRule = new ActivityTestRule<>(StartPage.class);
//
//    @Rule
//    public GrantPermissionRule mGrantPermissionRule =
//            GrantPermissionRule.grant(
//                    "android.permission.ACCESS_FINE_LOCATION");
//
//    @Test
//    public void bandAddUsers() {
//        ((StartPage) getCurrentActivity()).fabMenuClick();
//        // TODO: use mockDb
//        //((StartPage) getCurrentActivity()).button1Click();
//        ((StartPage) getCurrentActivity()).fabMenuClick();
//    }
//}
