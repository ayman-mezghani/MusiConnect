package ch.epfl.sdp.musiconnect.cloud;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseCloudStorageTest {

    private String username = "username";
    private String path = "path";
    private String saveName = "saveName";
    private FirebaseCloudStorage cloudStorage;

    @Mock
    StorageReference storageReference;

    @Mock
    Context context;

    @Mock
    File f;

    @Mock
    Uri fileUri;

    @Mock
    Task<StorageMetadata> metadataTask;

    @Mock
    UploadTask uploadTask;

    @Mock
    Task<Void> deleteTask;

    @Before
    public void init() {
        when(context.getCacheDir()).thenReturn(f);
        when(f.getPath()).thenReturn("Some path");

        cloudStorage = new FirebaseCloudStorage(context, storageReference);

        when(storageReference.child(anyString())).thenReturn(storageReference);
        when(storageReference.putFile(any(Uri.class), any(StorageMetadata.class))).thenReturn(uploadTask);
        when(storageReference.getMetadata()).thenReturn(metadataTask);
        when(storageReference.delete()).thenReturn(deleteTask);

        when(uploadTask.addOnSuccessListener(any())).thenReturn(uploadTask);
        when(uploadTask.addOnFailureListener(any())).thenReturn(uploadTask);

        when(metadataTask.addOnSuccessListener(any())).thenReturn(metadataTask);
        when(metadataTask.addOnFailureListener(any())).thenReturn(metadataTask);

        when(deleteTask.addOnSuccessListener(any())).thenReturn(deleteTask);
        when(deleteTask.addOnFailureListener(any())).thenReturn(deleteTask);
    }

    @Test
    public void uploadTest() throws IOException {
        cloudStorage.upload(CloudStorage.FileType.video, username, fileUri);

        verify(storageReference, times(1)).child(anyString());
        verify(storageReference, times(1)).putFile(eq(fileUri), any(StorageMetadata.class));
        verifyZeroInteractions(storageReference);

        verify(uploadTask, times(1)).addOnSuccessListener(any());
        verify(uploadTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(uploadTask);

        assertThrows(IOException.class, () -> cloudStorage.upload(null, null, null));
    }

    @Test
    public void downloadTest() throws IOException {
        cloudStorage.download(CloudStorage.FileType.video, username, any(CloudCallback.class));

        verify(storageReference, times(1)).child(anyString());
        verify(storageReference, times(1)).getMetadata();
        verifyZeroInteractions(storageReference);

        verify(metadataTask, times(1)).addOnSuccessListener(any());
        verify(metadataTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(metadataTask);
    }

    @Test
    public void deleteTest() throws IOException {
        cloudStorage.delete(path);

        verify(storageReference, times(1)).child(anyString());
        verify(storageReference, times(1)).delete();
        verifyZeroInteractions(storageReference);

        verify(deleteTask, times(1)).addOnSuccessListener(any());
        verify(deleteTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(deleteTask);
    }

}
