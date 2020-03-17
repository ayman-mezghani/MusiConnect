package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ch.epfl.sdp.R;

public class CloudStorage {
    private static final String TAG = "Cloud";
    private StorageReference storageReference;
    private final Context context;

    public CloudStorage(Context context) {
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
    }

    public void upload(Uri fileUri, String userName) {
        if (fileUri != null && fileUri.getPath().indexOf('/') > -1) {
            String filePath = fileUri.getPath();
            String cloudPath = userName + "/" + filePath.substring(filePath.lastIndexOf('/'));
            StorageReference fileRef = storageReference.child(cloudPath);

            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, R.string.cloud_upload_successful, Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, R.string.cloud_upload_failed, Toast.LENGTH_LONG).show();
                        Log.d(TAG, e.getMessage());
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                        String s = context.getString(R.string.cloud_uploading_file)+" ";
                        Toast.makeText(context, s + progress + "%", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, R.string.cloud_upload_invalid_file_path, Toast.LENGTH_LONG).show();
        }
    }


    public void download(String cloudPath) {
        StorageReference fileRef = storageReference.child(cloudPath);

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Local temp file has been created
                    Toast.makeText(context, R.string.cloud_download_successful, Toast.LENGTH_LONG).show();

                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(context, R.string.cloud_download_failed, Toast.LENGTH_LONG).show();
                    Log.d(TAG, e.getMessage());
                })
                .addOnProgressListener(taskSnapshot -> {
                    long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    String s = context.getString(R.string.cloud_downloading_file)+" ";
                    Toast.makeText(context, s + progress + "%", Toast.LENGTH_SHORT).show();
                });
    }
}
