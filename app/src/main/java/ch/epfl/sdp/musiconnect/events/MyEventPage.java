package ch.epfl.sdp.musiconnect.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbUserType;


public class MyEventPage extends EventPage {
    private static int LAUNCH_EVENT_EDIT_INTENT = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO
        // If event is from current user, show if visible or not to public

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        titleView = findViewById(R.id.eventTitle);
        creatorView = findViewById(R.id.eventCreatorField);
        addressView = findViewById(R.id.eventAddressField);
        timeView = findViewById(R.id.eventTimeField);
        participantsView = findViewById(R.id.eventParticipantsField);
        descriptionView = findViewById(R.id.eventDescriptionField);

        setupEditButton();
        setupDeleteButton();

        String eid = getIntent().getStringExtra("eid");
        retrieveEventInfo(eid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_EVENT_EDIT_INTENT && resultCode == Activity.RESULT_OK) {
            Handler handler = new Handler();
            handler.postDelayed(() -> retrieveEventInfo(data.getStringExtra("eid")), 500);
        }
    }


    private void setupEditButton() {
        Button editEvent = findViewById(R.id.btnEditEvent);
        editEvent.setOnClickListener(v -> {
            Intent intent = new Intent(MyEventPage.this, EventEditionPage.class);
            intent.putExtra("eid", getIntent().getStringExtra("eid"));
            intent.putExtra("title", titleView.getText().toString());
            intent.putExtra("address", addressView.getText().toString());
            intent.putExtra("description", descriptionView.getText().toString());
            intent.putExtra("visible", event.isVisible());
            intent.putStringArrayListExtra("emails", emails);
            intent.putExtra("datetime", timeView.getText().toString());

            startActivityForResult(intent, LAUNCH_EVENT_EDIT_INTENT);
        });
    }


    private void setupDeleteButton() {
        Button deleteEvent = findViewById(R.id.btnDeleteEvent);
        deleteEvent.setOnClickListener(v -> new AlertDialog.Builder(MyEventPage.this)
                .setMessage("Do you really want to delete this event?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    User m = event.getCreator();
                    m.removeEvent(event.getEid());

                    /*
                    if (m instanceof Musician) {
                        dbAdapter.update(DbUserType.Musician, m);
                    } else {
                        dbAdapter.update(DbUserType.Band, m);
                    }

                     */

                    dbAdapter.delete(DbUserType.Events, event);
                    MyEventPage.this.finish();

                    Toast.makeText(MyEventPage.this, "Deletion confirmed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(MyEventPage.this, "Deletion cancelled", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .create()
                .show());
    }
}
