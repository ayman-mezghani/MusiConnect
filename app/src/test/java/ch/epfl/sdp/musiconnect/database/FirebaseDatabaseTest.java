package ch.epfl.sdp.musiconnect.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDatabaseTest {

    private String collection = "collection";
    private String docName = "document";
    private FirebaseDatabase database;
    private Map<String, Object> map;


    @Mock
    FirebaseFirestore instance;

    @Mock
    CollectionReference colRef;

    @Mock
    DocumentReference docRef;

    @Mock
    SimplifiedDbEntry entry;

    @Mock
    SimplifiedEvent event;

    @Mock
    Task<Void> voidTask;

    @Mock
    Task<DocumentReference> docRefTask;

    @Mock
    Task<DocumentSnapshot> docSnapTask;

    @Mock
    DocumentSnapshot docSnap;

    @Mock
    Task<QuerySnapshot> querySnapTask;

    @Before
    public void init() {
        database = new FirebaseDatabase(instance);
        map = new HashMap<>();

        when(instance.collection(anyString())).thenReturn(colRef);
        when(colRef.document(anyString())).thenReturn(docRef);

        when(docRef.set(any(), any())).thenReturn(voidTask);
        when(colRef.add(any())).thenReturn(docRefTask);
        when(docRef.delete()).thenReturn(voidTask);
        when(docRef.update(any(Map.class))).thenReturn(voidTask);
        when(docRef.get()).thenReturn(docSnapTask);
        when(colRef.get()).thenReturn(querySnapTask);

        when(docSnapTask.isSuccessful()).thenReturn(true);
        when(docSnapTask.getResult()).thenReturn(docSnap);
        when(docSnap.exists()).thenReturn(true);

        when(docRefTask.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(docRefTask);
        when(docRefTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(docRefTask);

        when(voidTask.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(voidTask);
        when(voidTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(voidTask);
        when(docSnapTask.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(docSnapTask);
        when(docSnapTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(docSnapTask);
        when(querySnapTask.addOnCompleteListener(any(OnCompleteListener.class))).thenReturn(querySnapTask);

    }

    @Test
    public void addDocTest() {
        database.addDoc(collection, docName, entry);

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).document(eq(docName));
        verifyNoMoreInteractions(colRef);

        verify(docRef, times(1)).set(eq(entry), any(SetOptions.class));
        verifyNoMoreInteractions(docRef);

        verify(voidTask, times(1)).addOnSuccessListener(any());
        verify(voidTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(voidTask);
    }

    @Test
    public void addDocEventTest() {
        database.addDoc(event, DbDataType.Musician);

        verify(instance, times(1)).collection(eq("events"));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).add(eq(event));
        verifyNoMoreInteractions(colRef);

        verify(docRefTask, times(1)).addOnSuccessListener(any());
        verify(docRefTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(docRefTask);
    }

    @Test
    public void deleteDocTest() {
        database.deleteDoc(collection, docName);

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).document(eq(docName));
        verifyNoMoreInteractions(colRef);

        verify(docRef, times(1)).delete();
        verifyNoMoreInteractions(docRef);

        verify(voidTask, times(1)).addOnSuccessListener(any());
        verify(voidTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(voidTask);
    }

    @Test
    public void updateDocTest() {
        database.updateDoc(collection, docName, map);

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).document(eq(docName));
        verifyNoMoreInteractions(colRef);

        verify(docRef, times(1)).update(eq(map));
        verifyNoMoreInteractions(docRef);

        verify(voidTask, times(1)).addOnSuccessListener(any());
        verify(voidTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(voidTask);
    }

    @Test
    public void deleteFieldsInDocTest() {
        database.deleteFieldsInDoc(collection, docName, anyList());

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).document(eq(docName));
        verifyNoMoreInteractions(colRef);

        verify(docRef, times(1)).update(anyMap());
        verifyNoMoreInteractions(docRef);

        verify(voidTask, times(1)).addOnSuccessListener(any());
        verify(voidTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(voidTask);
    }

    @Test
    public void readDocTest() {
        database.readDoc(collection, docName, new DbCallback() {
        });

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).document(eq(docName));
        verifyNoMoreInteractions(colRef);

        verify(docRef, times(1)).get();
        verifyNoMoreInteractions(docRef);

        verify(docSnapTask, times(1)).addOnSuccessListener(any());
        verify(docSnapTask, times(1)).addOnFailureListener(any());
        verifyNoMoreInteractions(docSnapTask);
    }

    @Test
    public void docExistsTest() {
        database.docExists(collection, docName, new DbCallback() {
            @Override
            public void existsCallback(boolean exists) {
                assertTrue(exists);
            }
        });

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).document(eq(docName));
        verifyNoMoreInteractions(colRef);

        verify(docRef, times(1)).get();
        verifyNoMoreInteractions(docRef);

        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> listenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);

        verify(docSnapTask, times(1)).addOnCompleteListener(listenerCaptor.capture());
//        verifyNoMoreInteractions(voidTask);

        OnCompleteListener listener = listenerCaptor.getValue();
        listener.onComplete(docSnapTask);
    }

    @Test
    public void finderQueryTest() {
        database.finderQuery(collection, map, any(DbCallback.class));

        verify(instance, times(1)).collection(eq(collection));
        verifyNoMoreInteractions(instance);

        verify(colRef, times(1)).get();
        verifyNoMoreInteractions(colRef);

        verify(querySnapTask, times(1)).addOnCompleteListener(any());
        verifyNoMoreInteractions(querySnapTask);
    }
}
