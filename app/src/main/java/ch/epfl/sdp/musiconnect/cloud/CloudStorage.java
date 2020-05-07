package ch.epfl.sdp.musiconnect.cloud;

import android.net.Uri;

import java.io.IOException;

public interface CloudStorage {
    public enum FileType {
        profile_image, video;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    void upload(FileType fileType, String username, Uri fileUri) throws IOException;

    void download(FileType fileType, String username, CloudCallback cloudCallback);

    void delete(String cloudPath);
}
