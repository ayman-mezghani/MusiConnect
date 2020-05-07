package ch.epfl.sdp.musiconnect.cloud;

import android.net.Uri;

import java.io.IOException;

public class MockCloudStorage implements CloudStorage {

    @Override
    public void upload(FileType fileType, String username, Uri fileUri) throws IOException {

    }

    @Override
    public void download(FileType fileType, String username, CloudCallback cloudCallback) throws IOException {
        cloudCallback.onFailure();
    }

    @Override
    public void delete(String cloudPath) {
    }
}
