package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAdapter = DbGenerator.getDbInstance();

        setContentView(R.layout.activity_profile_page);

        mVideoView = findViewById(R.id.videoView);


        imgVw = findViewById(R.id.imgView);
        firstName = findViewById(R.id.myFirstname);
        lastName = findViewById(R.id.myLastname);
        username = findViewById(R.id.myUsername);
        email = findViewById(R.id.myMail);
        birthday = findViewById(R.id.myBirthday);

        Button editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(v -> {
                Intent profileModificationIntent = new Intent(this, ProfileModification.class);
                sendInformation(profileModificationIntent);
                // Permits sending information from child to parent activity
                startActivityForResult(profileModificationIntent, LAUNCH_PROFILE_MODIF_INTENT);

        });

        loadProfileContent();
        getVideoUri(userEmail);
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
        try {                                           //wait for async thread to fetch cached profile
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (checkConnection(MyProfilePage.this)) {              //gets profile info from database
            dbAdapter.read(DbUserType.Musician, CurrentUser.getInstance(this).email, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    Musician m = (Musician) user;
                    firstName.setText(m.getFirstName());
                    lastName.setText(m.getLastName());
                    username.setText(m.getUserName());
                    email.setText(m.getEmailAddress());
                    MyDate date = m.getBirthday();
                    String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
                    birthday.setText(s);
                    if (currentCachedUser == null || !ProfileModification.changeStaged) {            //if user profile isn't cached,cache it
                        mExecutor.execute(() -> {
                            mdao.insertAll(m);
                        });
                    }
                }
            });

        } else {
            if (currentCachedUser == null) {
                Toast.makeText(this, "Unable to fetch profile information; please connect to internet", Toast.LENGTH_LONG).show();
            } else {                                        //set profile info based on cache
                firstName.setText(currentCachedUser.getFirstName());
                lastName.setText(currentCachedUser.getLastName());
                username.setText(currentCachedUser.getUserName());
                email.setText(currentCachedUser.getEmailAddress());
                MyDate date = currentCachedUser.getBirthday();
                String s = date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
                birthday.setText(s);
            }
        }

    }


    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //        getVideoUri(userEmail);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LAUNCH_PROFILE_MODIF_INTENT && resultCode == Activity.RESULT_OK) {
            String[] newFields = data.getStringArrayExtra("newFields");
            assert newFields != null;
            firstName.setText(newFields[0]);
            lastName.setText(newFields[1]);
            username.setText(newFields[2]);
            email.setText(newFields[3]);
            birthday.setText(newFields[4]);
            String videoUriString = data.getStringExtra("videoUri");

            if (videoUriString != null) {
                videoUri = Uri.parse(videoUriString);
                showVideo();
            } else {
                getVideoUri(userEmail);
            }
        }

//        TODO: refresh the intent, may be useful after video change
//        finish();
//        overridePendingTransition( 0, 0);
//        startActivity(getIntent());
//        overridePendingTransition( 0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.my_profile)
            return true;
        else
            super.onOptionsItemSelected(item);
        return true;
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
        intent.putExtra("FIRST_NAME", firstName.getText().toString());
        intent.putExtra("LAST_NAME", lastName.getText().toString());
        intent.putExtra("USERNAME", username.getText().toString());
        intent.putExtra("MAIL", email.getText().toString());
        intent.putExtra("BIRTHDAY", birthday.getText().toString());
    }
}