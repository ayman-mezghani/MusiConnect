package ch.epfl.sdp.musiconnect.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Band;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.events.EventListPage;

public class VisitorProfilePage extends ProfilePage implements DbCallback {

    private Button contactButton;
    private Button eventListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbSingleton.getDbInstance();

        setContentView(R.layout.activity_visitor_profile_page);
        mVideoView = findViewById(R.id.videoView);

        imgVw = findViewById(R.id.imgView);

        titleView = findViewById(R.id.visitorProfileTitle);
        firstNameView = findViewById(R.id.visitorProfileFirstname);
        lastNameView = findViewById(R.id.visitorProfileLastname);
        usernameView = findViewById(R.id.visitorProfileUsername);
        emailView = findViewById(R.id.visitorProfileEmail);
        birthdayView = findViewById(R.id.visitorProfileBirthday);

        Intent intent = getIntent();
        if (intent.hasExtra("UserEmail")) {
            userEmail = intent.getStringExtra("UserEmail");
        }

        eventListButton = findViewById(R.id.btnVisitorEventList);
        contactButton = findViewById(R.id.btnContactMusician);

        Button addUserToBand = findViewById(R.id.add_user_to_band);
        if (CurrentUser.getInstance(this).getTypeOfUser() == TypeOfUser.Band) {
            addUserToBand.setVisibility(View.VISIBLE);
            addUserToBand.setFocusable(true);
            addUserToBand.setClickable(true);
        }

        addUserToBand.setOnClickListener(v -> addUserToBand());

        loadProfileContent();
        getVideoUri(userEmail);
        setupEventListButton();

        contactButton.setOnClickListener(view -> sendEmail(userEmail, getResources().getString(R.string.musiconnect_contact_mail)));

        instrument = findViewById(R.id.visitorProfileInstrument);
        selectedInstrument = findViewById(R.id.visitorProfileSelectedInstrument);
        level = findViewById(R.id.visitorProfileLevel);
        selectedLevel = findViewById(R.id.visitorProfileSelectedLevel);

        contactButton.setOnClickListener(view -> sendEmail(userEmail, getResources().getString(R.string.musiconnect_contact_mail)));
    }

    // Some of this method code is inspired from stackoverflow.com
    @SuppressLint("IntentReset")
    protected void sendEmail(String to, String subject) {
        String[] TO = {to};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        try {
            startActivity(Intent.createChooser(emailIntent, "Sending mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUserToBand() {
        Band b = CurrentUser.getInstance(this).getBand();
        if (b != null && !b.containsMember(userEmail)) {
            b.addMember(userEmail);
        }
        (DbSingleton.getDbInstance()).add(DbDataType.Band, b);
    }

    @Override
    protected void loadUserProfile(User user) {
        Musician m = (Musician) user;
        String sTitle = m.getUserName() + "'s profile";
        titleView.setText(sTitle);

        firstNameView.setText(m.getFirstName());
        lastNameView.setText(m.getLastName());
        usernameView.setText(m.getUserName());

        MyDate date = m.getBirthday();
        String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
        birthdayView.setText(s);

        emailView.setText(userEmail);

        addFirstNameToContactButtonText(m.getFirstName());

        if (!m.getInstruments().isEmpty()) {
            Instrument instr = (Instrument) m.getInstruments().keySet().toArray()[0];
            String i = instr.toString().substring(0, 1).toUpperCase() + instr.toString().substring(1);
            selectedInstrument.setText(i);

            Level lvl = m.getInstruments().get(instr);
            String l = lvl.toString().substring(0, 1).toUpperCase() + lvl.toString().substring(1);
            selectedLevel.setText(l);
        }
    }

    private void setupEventListButton() {
        eventListButton.setOnClickListener(v -> {
            Intent intent = new Intent(VisitorProfilePage.this, EventListPage.class);
            intent.putExtra("UserEmail", userEmail);

            VisitorProfilePage.this.startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void addFirstNameToContactButtonText(String firstName) {
        contactButton.setText("Contact " + firstName);
    }
}
