package ch.epfl.sdp.musiconnect;


import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class VideoRecordingTests {

    @Rule
    public ActivityTestRule<StartPage> mActivityTestRule = new ActivityTestRule<>(StartPage.class);

    @Test
    public void videoRecordingTests() throws InterruptedException {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition( childAtPosition(withId(R.id.action_bar),1),2), isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.title), withText("My profile"),
                        childAtPosition(childAtPosition(withId(R.id.content),0),0),isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withText("Capture Video"),
                        childAtPosition(childAtPosition(withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),0),1), isDisplayed()));
        appCompatButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
