package ch.epfl.sdp.musiconnect.database;

import com.google.firebase.firestore.Query;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Objects;

abstract class DatabaseQueryHelpers {
    static Query unpack(Query collectionRef, Map<String, Object> args) {
        if (args.isEmpty())
            return collectionRef;
        else {
            ArrayDeque<Map.Entry<String, Object>> argEntries = new ArrayDeque<>(args.entrySet());
            Query res = singleQuery(collectionRef, Objects.requireNonNull(argEntries.pollFirst()));
            while (!argEntries.isEmpty()) {
                res = singleQuery(res, Objects.requireNonNull(argEntries.pollFirst()));
            }
            return res;
        }
    }

    static Query singleQuery(Query prev, Map.Entry<String, Object> clause) {
        if (clause.getValue() != null) {
//            Log.d("checkcheck", clause.getKey() + "=>" + clause.getValue() + " " + clause.getValue().getClass().getName());
            if (clause.getKey().equals("")) { //@TODO for instruments, they're not being saved yet
                return prev.whereArrayContains(clause.getKey(), clause.getValue());
            } else {
                return prev.whereEqualTo(clause.getKey(), clause.getValue());
            }
        } else return prev;
    }
}
