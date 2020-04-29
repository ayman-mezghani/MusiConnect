package ch.epfl.sdp.musiconnect.cloud;

import android.net.Uri;

public interface CloudCallback {
    void onSuccess(Uri fileUri);
    default void onFailure() {
    }

}
