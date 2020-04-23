package ch.epfl.sdp.musiconnect.cloud;

import android.net.Uri;

import java.io.IOException;

public class MockCloudStorage implements CloudStorage {

    @Override
    public void upload(Uri fileUri, FirebaseCloudStorage.FileType fileType, String userName) throws IOException {
    }

    @Override
    public void download(String cloudPath, String saveName, CloudCallback cloudCallback) throws IOException {
        cloudCallback.onFailure();
    }

    @Override
    public void delete(String cloudPath) {
    }
}
