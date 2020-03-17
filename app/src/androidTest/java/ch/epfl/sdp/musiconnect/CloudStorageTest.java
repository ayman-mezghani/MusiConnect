package ch.epfl.sdp.musiconnect;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

public class CloudStorageTest {
    /*@Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);*/
    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
            GrantPermissionRule.grant(android.Manifest
                    .permission.READ_EXTERNAL_STORAGE);

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private CloudStorage storage = new CloudStorage(context);

    @Test
    public void uploadTest() {
        storage.uploadGoogle("/storage/1217-3717/image.jpg");
    }
}
