package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseQueryHelpersTest {

    Map<String, Object> map;

    @Mock
    Query query;

    @Before
    public void init() {
        map = new HashMap<String, Object>();
        when(query.whereEqualTo(anyString(), any())).thenReturn(query);
        when(query.whereArrayContains(anyString(), any())).thenReturn(query);
    }

    @Test
    public void singleQueryNullValueTest() {
        map.put("toto", null);
        ArrayDeque<Map.Entry<String, Object>> argEntries = new ArrayDeque<>(map.entrySet());
        Map.Entry<String, Object> entry = argEntries.poll();

        assertEquals(query, DatabaseQueryHelpers.singleQuery(query, Objects.requireNonNull(entry)));
        verifyZeroInteractions(query);
    }

    @Test
    public void singleQueryWhereEqualToTest() {
        map.put("toto", "toto");
        ArrayDeque<Map.Entry<String, Object>> argEntries = new ArrayDeque<>(map.entrySet());
        Map.Entry<String, Object> entry = argEntries.poll();

        assertEquals(query, DatabaseQueryHelpers.singleQuery(query, Objects.requireNonNull(entry)));
        verify(query, times(1)).whereEqualTo(entry.getKey(), entry.getValue());
        verifyNoMoreInteractions(query);
    }

    @Test
    public void singleQueryWhereArrayContainsTest() {
        map.put("", "toto");
        ArrayDeque<Map.Entry<String, Object>> argEntries = new ArrayDeque<>(map.entrySet());
        Map.Entry<String, Object> entry = argEntries.poll();

        assertEquals(query, DatabaseQueryHelpers.singleQuery(query, Objects.requireNonNull(entry)));
        verify(query, times(1)).whereArrayContains(entry.getKey(), entry.getValue());
        verifyNoMoreInteractions(query);
    }

    @Test
    public void unpackTest() {
        map.put("", "toto");
        map.put("toto", "toto");
        map.put("alice", "alice");
        map.put("null", null);

        assertEquals(query, DatabaseQueryHelpers.unpack(query, map));
        verify(query, times(1)).whereArrayContains("", "toto");
        verify(query, times(1)).whereEqualTo("toto", "toto");
        verify(query, times(1)).whereEqualTo("alice", "alice");
        verifyNoMoreInteractions(query);
    }

    @Test
    public void unpackEmptyTest() {
        assertEquals(query, DatabaseQueryHelpers.unpack(query, map));
        verifyNoMoreInteractions(query);
    }
}

