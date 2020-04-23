package ch.epfl.sdp.musiconnect.cloud;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import ch.epfl.sdp.R;

public class FirebaseCloudStorage implements CloudStorage {

    private static final String TAG = "Cloud";
    private StorageReference storageReference;
    private final Context context;

    FirebaseCloudStorage(Context context) {
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
    }

    public void upload(Uri fileUri, FileType fileType, String userName) throws IOException {
        if (fileUri != null) {
            String cloudPath = userName + "/" + fileType;

            StorageReference fileRef = storageReference.child(cloudPath);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("video/mp4")
                    .build();

            fileRef.putFile(fileUri, metadata)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, R.string.cloud_upload_successful, Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        //Toast.makeText(context, R.string.cloud_upload_failed, Toast.LENGTH_LONG).show();
                        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    });
        } else {
            throw new IOException();
        }
    }


    public void download(String cloudPath, String saveName, CloudCallback cloudCallback) throws
            IOException {
        StorageReference fileRef = storageReference.child(cloudPath);

        File localFile = File.createTempFile(saveName + "_", null, null);

        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Local temp file has been created
//                    Toast.makeText(context, R.string.cloud_download_successful, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "File created. Created " + localFile.toString());
                    cloudCallback.onSuccess(Uri.fromFile(localFile));
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
//                    Toast.makeText(context, R.string.cloud_download_failed, Toast.LENGTH_LONG).show();
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    cloudCallback.onFailure();
                });
    }

    public void delete(String cloudPath) {
        StorageReference desertRef = storageReference.child(cloudPath);

        desertRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, R.string.cloud_delete_successful, Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(context, R.string.cloud_delete_failed, Toast.LENGTH_LONG).show();
                });
    }
}
