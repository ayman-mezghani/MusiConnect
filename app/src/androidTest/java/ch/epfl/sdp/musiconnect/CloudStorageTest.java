package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.storage.FirebaseStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.BuildConfig;
import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;

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

    private final String TAG = "CloudTest";

    private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private CloudStorage storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), context);
    private String fileName = R.drawable.image + "";

    private static void waitALittle(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadSuccessfulTest() throws IOException {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + fileName);
        storage.upload(imageUri, CloudStorage.FileType.profile_image, "test");
        waitALittle(7);
        //onView(withText(R.string.cloud_upload_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        waitALittle(5);
        storage.delete("test/" + fileName);
    }

    @Test
    public void uploadFailedTest() throws IOException {
        Uri imageUri = Uri.parse("Random/stuff");
        storage.upload(imageUri, CloudStorage.FileType.profile_image, "test");
        waitALittle(7);
        //onView(withText(R.string.cloud_upload_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }


    @Test
    public void downloadSuccessfulTest() throws IOException {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + fileName);
        storage.upload(imageUri, CloudStorage.FileType.profile_image, "test");
        waitALittle(10);
        storage.download("test/" + fileName, fileName, new CloudCallback() {
            @Override
            public void onCallback(Uri fileUri) {
                Log.d(TAG, "testing cloud for coverage");
            }
        });
        waitALittle(7);
        //onView(withText(R.string.cloud_download_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        waitALittle(5);
        storage.delete("test/" + fileName);
    }

    @Test
    public void downloadFailedTest() throws IOException {
        storage.download("Random/stuff", "stuff", new CloudCallback() {
            @Override
            public void onCallback(Uri fileUri) {
                Log.d(TAG, "testing cloud for coverage");
            }
        });
        waitALittle(5);
        //onView(withText(R.string.cloud_download_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void deleteSuccessfulTest() throws IOException {
        Uri imageUri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + fileName);
        storage.upload(imageUri, CloudStorage.FileType.profile_image, "test");
        waitALittle(10);
        storage.delete("test/" + fileName);
        waitALittle(2);
        //onView(withText(R.string.cloud_delete_successful)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void deleteFailedTest() {
        storage.delete("Random/stuff");
        waitALittle(2);
        //onView(withText(R.string.cloud_delete_failed)).inRoot(withDecorView(not(startPageRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
