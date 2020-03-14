package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CloudStorage {
    private static final String TAG = "Cloud";
    private StorageReference storageReference;
    private final Context context;

    public CloudStorage(Context context) {
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
    }

    public void uploadGoogle(String filePath) {
        final StorageReference ref = storageReference.child("images/mountains.jpg");

        Uri uri = Uri.fromFile(new File(filePath));
        StorageReference fileRef = storageReference.child("p.png");

        Log.d("Cloud", uri.getPath());

//        Log.d("Cloud", uri.toString());

        UploadTask uploadTask = ref.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "File uploaded", Toast.LENGTH_LONG).show();
            } else {
                // Handle failures
                // ...
                Toast.makeText(context, "File NOOOOOOOT uploaded", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void upload(String filePath, String userName) {
        if (filePath != null && !filePath.isEmpty()) {
            Uri file = Uri.fromFile(new File(filePath));
            StorageReference fileRef = storageReference.child("p.png");

            Log.d("Cloud", file.getPath());


            //ProgressDialog progressDialog = new ProgressDialog(context);
            //progressDialog.setTitle("Uploading...");
            //progressDialog.show();

            fileRef.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> {
                        //progressDialog.dismiss();
                        Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
//                        progressDialog.dismiss();
                        Toast.makeText(context, "File upload failed successfully", Toast.LENGTH_LONG).show();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
//                        long progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
//                        progressDialog.setMessage(progress + "% Uploaded...");
                    });
        } else {
//            Display error Toast
            Toast.makeText(context, "No file is selected for upload", Toast.LENGTH_LONG).show();
        }
    }
}
