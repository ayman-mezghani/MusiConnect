package ch.epfl.sdp.musiconnect;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sdp.musiconnect.testsFunctions.waitALittle;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class fabClick {

    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Test
    public void fabClick() {

        onView(withId(R.id.fab_menu)).perform(scrollTo(), click());
        waitALittle(2);


        onView(withId(R.id.fab_button_2)).perform(click());
        /*
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_menu),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0),
                                0)));
        floatingActionButton.perform(scrollTo(), click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab_button_2),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0),
                                3)));
        floatingActionButton2.perform(scrollTo(), click());
         */
    }

    }
