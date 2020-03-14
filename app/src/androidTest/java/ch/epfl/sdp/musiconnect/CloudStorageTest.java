package ch.epfl.sdp.musiconnect;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class CloudStorageTest {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private CloudStorage storage = new CloudStorage(context);

    @Test
    public void uploadTest() {
        storage.upload("/home/ayman/Pictures/p.png", "amz");
    }
}
