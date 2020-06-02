package ch.epfl.sdp.musiconnect;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.cloud.FirebaseCloudStorage;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.pages.StartPage;

import static org.junit.Assert.assertEquals;

public class CloudStorageTests {
    @Rule
    public final ActivityTestRule<StartPage> mActivityTestRule =
            new ActivityTestRule<>(StartPage.class);

    @Test
    public void prodInstantiationTest() {
        CloudStorageSingleton.flush();
        CloudStorage cs = CloudStorageSingleton.getCloudInstance(mActivityTestRule.getActivity());
        assertEquals(FirebaseCloudStorage.class.getName(), cs.getClass().getName());
    }

    @Test
    public void mockInstantiationTest() {
        CloudStorageSingleton.setStorage(new MockCloudStorage());
        CloudStorage cs = CloudStorageSingleton.getCloudInstance(mActivityTestRule.getActivity());
        assertEquals(MockCloudStorage.class.getName(), cs.getClass().getName());
    }
}
