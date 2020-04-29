package ch.epfl.sdp.musiconnect.cloud;

import android.net.Uri;

import java.io.IOException;

public interface CloudStorage {
    public enum FileType {
        profile_image, video
    }

    void upload(Uri fileUri, FirebaseCloudStorage.FileType fileType, String userName) throws IOException;

    void download(String cloudPath, String saveName, CloudCallback cloudCallback) throws IOException;

    void delete(String cloudPath);
}
