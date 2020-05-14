package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static ch.epfl.sdp.musiconnect.ConnectionCheck.checkConnection;

public class MyProfilePage extends ProfilePage implements View.OnClickListener {
    private static int LAUNCH_PROFILE_MODIF_INTENT = 102;
    private DbAdapter dbAdapter;

    private Musician currentCachedUser;

    private TextView instrument;
    private TextView selectedInstrument;
    private TextView level;
    private TextView selectedLevel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbGenerator.getDbInstance();

        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);
        imgVw = findViewById(R.id.imgView);
        firstNameView = findViewById(R.id.myFirstname);
        lastNameView = findViewById(R.id.myLastname);
        usernameView = findViewById(R.id.myUsername);
        emailView = findViewById(R.id.myMail);
        birthdayView = findViewById(R.id.myBirthday);

        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
                Intent profileModificationIntent = new Intent(this, ProfileModification.class);
                sendInformation(profileModificationIntent);
                // Permits sending information from child to parent activity
                startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);

        });

        loadProfileContent();
        getVideoUri(userEmail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setSelectedItemId(R.id.my_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        instrument = findViewById(R.id.myProfileInstrument);
        selectedInstrument = findViewById(R.id.myProfileSelectedInstrument);
        level = findViewById(R.id.myProfileLevel);
        selectedLevel = findViewById(R.id.myProfileSelectedLevel);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_profile)
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void loadProfileContent() {
        Executor mExecutor = Executors.newSingleThreadExecutor();
        AppDatabase localDb = AppDatabase.getInstance(this);
        MusicianDao mdao = localDb.musicianDao();
        userEmail = CurrentUser.getInstance(this).email;
      
        //fetches the current user's profile
        mExecutor.execute(() -> {
            List<Musician> result = mdao.loadAllByIds(new String[]{userEmail});
            currentCachedUser = result.isEmpty() ? null : result.get(0);
        });

        try { // wait for async thread to fetch cached profile
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // gets profile info from database
        if (checkConnection(MyProfilePage.this)) {
            dbAdapter.read(DbUserType.Musician, CurrentUser.getInstance(this).email, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    Musician m = (Musician) user;
                    firstNameView.setText(m.getFirstName());
                    lastNameView.setText(m.getLastName());
                    usernameView.setText(m.getUserName());
                    emailView.setText(m.getEmailAddress());
                    MyDate date = m.getBirthday();
                    String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
                    birthdayView.setText(s);
                    // if user profile isn't cached,cache it
                    if (currentCachedUser == null || !ProfileModification.changeStaged) {
                        mExecutor.execute(() -> {
                            mdao.insertAll(m);
                        });
                    }
                }
            });

        } else {
            if (currentCachedUser == null) {
                Toast.makeText(this, "Unable to fetch profile information; please connect to internet", Toast.LENGTH_LONG).show();
            } else { // set profile info based on cache
                firstNameView.setText(currentCachedUser.getFirstName());
                lastNameView.setText(currentCachedUser.getLastName());
                usernameView.setText(currentCachedUser.getUserName());
                emailView.setText(currentCachedUser.getEmailAddress());
                MyDate date = currentCachedUser.getBirthday();

                String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
                birthdayView.setText(s);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LAUNCH_PROFILE_MODIF_INTENT && resultCode == Activity.RESULT_OK) {
            String[] newFields = data.getStringArrayExtra("newFields");
            assert newFields != null;
            firstNameView.setText(newFields[0]);
            lastNameView.setText(newFields[1]);
            usernameView.setText(newFields[2]);
            emailView.setText(newFields[3]);
            birthdayView.setText(newFields[4]);

            String videoUriString = data.getStringExtra("videoUri");

            if (videoUriString != null) {
                videoUri = Uri.parse(videoUriString);
                showVideo();
            } else {
                getVideoUri(userEmail);
            }
        }
    }

    public void onClick(View view) {
        super.displayNotFinishedFunctionalityMessage();
    }


    /**
     * Automatically fill the edit texts of profile modification page with actual string values
     *
     * @param intent
     */
    private void sendInformation(Intent intent) {
        intent.putExtra("FIRST_NAME", firstNameView.getText().toString());
        intent.putExtra("LAST_NAME", lastNameView.getText().toString());
        intent.putExtra("USERNAME", usernameView.getText().toString());
        intent.putExtra("MAIL", emailView.getText().toString());
        intent.putExtra("BIRTHDAY", birthdayView.getText().toString());
    }
}