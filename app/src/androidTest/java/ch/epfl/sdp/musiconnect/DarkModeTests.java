package ch.epfl.sdp.musiconnect;

import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DarkModeTests {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }

    @Test
    public void checkDefaultModeIsLight() {
        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_black)));
    }

    @Test
    public void checkThatThemeChangesWhenSwitchIsToggled() {
        TextView title = startPageRule.getActivity().findViewById(R.id.welcomeText);
        onView(withId(R.id.darkModeSwitch)).perform(scrollTo(), setChecked(true));
        assertThat(title.getCurrentTextColor(), is(ContextCompat.getColor(startPageRule.getActivity().getApplicationContext(), R.color.text_grey)));
    }
}
