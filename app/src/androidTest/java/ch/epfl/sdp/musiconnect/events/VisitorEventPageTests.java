package ch.epfl.sdp.musiconnect.events;


import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.MockCloudStorage;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.MockDatabase;

@RunWith(AndroidJUnit4.class)
public class VisitorEventPageTests {
    @Rule
    public final ActivityTestRule<VisitorEventPage> eventPageRule =
            new ActivityTestRule<>(VisitorEventPage.class, true, false);

    private static MockDatabase md;

    @BeforeClass
    public static void setMocks() {
        md = new MockDatabase();
        DbGenerator.setDatabase(md);
        CloudStorageGenerator.setStorage((new MockCloudStorage()));
    }


    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() { Intents.release(); }


    @Test
    public void test() {
        Intent intent = new Intent();
        intent.putExtra("eid", "2");
        eventPageRule.launchActivity(intent);
    }


}
