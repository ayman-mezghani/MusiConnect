package ch.epfl.sdp.musiconnect.cloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import ch.epfl.sdp.R;

public class FirebaseCloudStorage implements CloudStorage {

    private static final String TAG = "FirebaseCloudStorage";
    private static final String METADATA_DATE = "Upload Date";
    private StorageReference storageReference;
    private final Context context;
    private final String cacheDirPath;

    FirebaseCloudStorage(Context context) {
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
        this.cacheDirPath = context.getCacheDir().getPath();
    }

    FirebaseCloudStorage(Context context, StorageReference storageReference) {
        this.storageReference = storageReference;
        this.context = context;
        this.cacheDirPath = context.getCacheDir().getPath();
    }

    public void upload(FileType fileType, String username, Uri fileUri) throws IOException {
        if (fileUri != null) {

            String cloudPath = username + "/" + fileType;
            StorageReference fileRef = storageReference.child(cloudPath);

            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String today = dateFormat.format(date);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("video/mp4")
                    .setCustomMetadata(METADATA_DATE, today)
                    .build();

            fileRef.putFile(fileUri, metadata)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, R.string.cloud_upload_successful, Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, R.string.cloud_upload_failed, Toast.LENGTH_LONG).show();
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    });
        } else {
            throw new IOException();
        }
    }

    public void download(FileType fileType, String username, CloudCallback cloudCallback) {

        String cloudPath = username + "/" + fileType;
        StorageReference fileRef = storageReference.child(cloudPath);

        fileRef.getMetadata()
                .addOnSuccessListener(storageMetadata -> {
                    String metadataTag = storageMetadata.getCustomMetadata(METADATA_DATE);
                    String dirPath = cacheDirPath + "/" + fileType;
                    String localFileNamePattern = username + "_" + fileType;
                    String localFileName = localFileNamePattern + "_" + metadataTag;

                    File cacheDirectory = new File(dirPath);

                    if (!cacheDirectory.exists())
                        cacheDirectory.mkdir();

//                    Log.d(TAG, directory.exists() + "");
//
//                    String[] paths = directory.list();
//
//                    for (String p : paths)
//                        Log.d(TAG, p);

                    File localFile = new File(cacheDirectory, localFileName);

                    if (!localFile.exists()) {
                        File[] matchingFiles = cacheDirectory.listFiles((dir, name) -> name.startsWith(localFileNamePattern));

                        if (matchingFiles != null) {
                            for (File f : matchingFiles)
                                f.delete();
                        }

                        downloader(fileRef, localFile, cloudCallback);
                    } else {
                        cloudCallback.onSuccess(Uri.fromFile(localFile));
                    }

                }).addOnFailureListener(e -> {
            DownloadFailureRoutine(e, cloudCallback);
        });
    }

    public void delete(String cloudPath) {
        StorageReference desertRef = storageReference.child(cloudPath);

        desertRef.delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, R.string.cloud_delete_successful, Toast.LENGTH_LONG).show())
                .addOnFailureListener(exception -> Toast.makeText(context, R.string.cloud_delete_failed, Toast.LENGTH_LONG).show());
    }

    private void downloader(StorageReference fileRef, File localFile, CloudCallback cloudCallback) {
        try {
            localFile.createNewFile();
            fileRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG, "File created. Created " + localFile.toString());
                        cloudCallback.onSuccess(Uri.fromFile(localFile));
                    })
                    .addOnFailureListener(e -> {
                        DownloadFailureRoutine(e, cloudCallback);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void DownloadFailureRoutine(Exception e, CloudCallback cloudCallback) {
        Toast.makeText(context, R.string.cloud_download_failed, Toast.LENGTH_LONG).show();
        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        cloudCallback.onFailure();
    }
}
