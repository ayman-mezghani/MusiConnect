package ch.epfl.sdp.musiconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

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
            String cloudPath = userName + "/" + filePath.substring(filePath.lastIndexOf('/'));
            StorageReference fileRef = storageReference.child(cloudPath);

            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "File upload failed", Toast.LENGTH_LONG).show();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                        Toast.makeText(context, "Uploading: " + progress + "%", Toast.LENGTH_SHORT).show();
                    });
        } else {
//            Display error Toast
            Toast.makeText(context, "No file is selected for upload", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_LONG).show();

                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(context, "File download failed", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    Toast.makeText(context, "Downloading: " + progress + "%", Toast.LENGTH_SHORT).show();
                });
    }
}
