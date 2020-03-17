package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.BuildConfig;
import ch.epfl.sdp.R;

import static android.provider.Settings.System.getString;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;

public class CloudStorageTest {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private CloudStorage storage = new CloudStorage(context);

    private static void waitALittle(int t) {
        try {
            TimeUnit.MILLISECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadSuccessfulTest() {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.image);
        storage.upload(imageUri, "amz");
        waitALittle(7000);
        onView(withText(R.string.cloud_upload_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void uploadFailedTest() {
        Uri imageUri = Uri.parse("Random/stuff");
        storage.upload(imageUri, "amz");
        waitALittle(6000);
        onView(withText(R.string.cloud_upload_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }


    @Test
    public void downloadSuccessfulTest() {
        storage.download("test/image.jpg");
        waitALittle(7000);
        onView(withText(R.string.cloud_download_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void downloadFailedTest() {
        storage.download("Random/stuffg");
        waitALittle(5000);
        onView(withText(R.string.cloud_download_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
