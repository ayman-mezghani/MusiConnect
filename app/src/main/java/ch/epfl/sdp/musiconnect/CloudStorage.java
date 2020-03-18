package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

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
        if (fileUri != null) {

            String filePath = fileUri.getPath();
//            File f = new File(Objects.requireNonNull(filePath));
//            if (f.exists()) {
            String cloudPath = userName + "/" + getFileName(filePath);
            StorageReference fileRef = storageReference.child(cloudPath);

            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, R.string.cloud_upload_successful, Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, R.string.cloud_upload_failed, Toast.LENGTH_LONG).show();
                        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                        String s = context.getString(R.string.cloud_uploading_file) + " ";
                        Toast.makeText(context, s + progress + "%", Toast.LENGTH_SHORT).show();
                    });
//            } else {
//                Toast.makeText(context, R.string.cloud_upload_invalid_file_path, Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(context, R.string.cloud_upload_invalid_file_path, Toast.LENGTH_LONG).show();
        }
    }


    public void download(String cloudPath, String fullSavePath) {
        StorageReference fileRef = storageReference.child(cloudPath);

        String dirPath = fullSavePath;

        if (fullSavePath.charAt(fullSavePath.length()-1) != '/')
            dirPath += "/";

        File localFile = new File(dirPath + getFileName(cloudPath));

        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Local temp file has been created
                    Toast.makeText(context, R.string.cloud_download_successful, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "File created. Created " + localFile.toString());
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(context, R.string.cloud_download_failed, Toast.LENGTH_LONG).show();
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                })
                .addOnProgressListener(taskSnapshot -> {
                    long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    String s = context.getString(R.string.cloud_downloading_file) + " ";
                    Toast.makeText(context, s + progress + "%", Toast.LENGTH_SHORT).show();
                });
    }

    public void delete(String cloudPath) {
        StorageReference desertRef = storageReference.child(cloudPath);

        desertRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // File deleted successfully
                    Toast.makeText(context, R.string.cloud_delete_successful, Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    // Uh-oh, an error occurred!
                    Toast.makeText(context, R.string.cloud_delete_failed, Toast.LENGTH_LONG).show();
                });
    }

    private String getFileName(@NonNull String path) {
        return path.contains("/") ? path.substring(path.lastIndexOf('/')) : path;
    }
}
