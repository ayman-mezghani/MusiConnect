package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.BuildConfig;
import ch.epfl.sdp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public class CloudStorageTest {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Before
    public void stallBefore() {
        waitALittle(5);
    }

    @After
    public void stallAfter() {
        waitALittle(5);
    }

    private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private CloudStorage storage = new CloudStorage(context);
    private String fileName = R.drawable.image + "";

    private static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadSuccessfulTest() {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + fileName);
        storage.upload(imageUri, "test");
        waitALittle(7);
        // onView(withText(R.string.cloud_upload_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        waitALittle(5);
        storage.delete("test/" + fileName);
    }

    @Test
    public void uploadFailedTest() {
        Uri imageUri = Uri.parse("Random/stuff");
        storage.upload(imageUri, "test");
        waitALittle(7);
        // onView(withText(R.string.cloud_upload_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }


    @Test
    public void downloadSuccessfulTest() {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + fileName);
        storage.upload(imageUri, "test");
        waitALittle(10);
        storage.download("test/" + fileName);
        waitALittle(7);
        // onView(withText(R.string.cloud_download_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        waitALittle(5);
        storage.delete("test/" + fileName);
    }

    @Test
    public void downloadFailedTest() {
        storage.download("Random/stuffg");
        waitALittle(5);
        // onView(withText(R.string.cloud_download_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void deleteSuccessfulTest() {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + fileName);
        storage.upload(imageUri, "test");
        waitALittle(10);
        storage.delete("test/" + fileName);
        waitALittle(2);
        // onView(withText(R.string.cloud_delete_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void deleteFailedTest() {
        storage.delete("Random/stuffg");
        waitALittle(2);
        // onView(withText(R.string.cloud_delete_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}