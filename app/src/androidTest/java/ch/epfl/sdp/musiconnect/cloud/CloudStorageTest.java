package ch.epfl.sdp.musiconnect.cloud;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.musiconnect.StartPage;

import static org.junit.Assert.assertEquals;

public class CloudStorageTest {
    @Rule
    public final ActivityTestRule<StartPage> startPageRule =
            new ActivityTestRule<>(StartPage.class);

    @Test
    public void prodInstantiationTest() {
        CloudStorageGenerator.flush();
        CloudStorage cs = CloudStorageGenerator.getCloudInstance(startPageRule.getActivity());
        assertEquals(FirebaseCloudStorage.class.getName(), cs.getClass().getName());
    }

    @Test
    public void mockInstantiationTest() {
        CloudStorageGenerator.setStorage(new MockCloudStorage());
        CloudStorage cs = CloudStorageGenerator.getCloudInstance(startPageRule.getActivity());
        assertEquals(MockCloudStorage.class.getName(), cs.getClass().getName());
    }
}
