package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.SimplifiedMusician;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static ch.epfl.sdp.musiconnect.ConnectionCheck.checkConnection;
import static ch.epfl.sdp.musiconnect.roomdatabase.MyDateConverter.dateToMyDate;

public class ProfileModification extends ProfilePage implements View.OnClickListener {

    String firstName, lastName, username, mail, birthday;
    EditText[] editFields;
    final Calendar calendar = Calendar.getInstance();

    protected static int VIDEO_REQUEST = 101;

    private CloudStorage storage;
    private boolean videoRecorded = false;

    private List<Musician> result; //used to fetch from room database
    public static boolean changeStaged = false;    //indicates if there are changes not commited to online database yet


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modification);

        editFields = new EditText[] {findViewById(R.id.newFirstName), findViewById(R.id.newLastName), findViewById(R.id.newUsername), findViewById(R.id.newEmailAddress), findViewById(R.id.newBirthday)};

        manageDatePickerDialog(editFields[4]);

        onCreateGetIntentsFields();

        setEditTextFields(editFields, new String[]{firstName, lastName, username, mail, birthday});

        findViewById(R.id.btnSaveProfile).setOnClickListener(this);
        findViewById(R.id.btnDoNotSaveProfile).setOnClickListener(this);

        mVideoView = findViewById(R.id.videoViewEdit);
        findViewById(R.id.btnCaptureVideo).setOnClickListener(v -> captureVideo());

        getVideoUri(mail);
    }

    private void onCreateGetIntentsFields() {
        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        username = getIntent().getStringExtra("USERNAME");
        mail = getIntent().getStringExtra("MAIL");
        birthday = getIntent().getStringExtra("BIRTHDAY");
        initCalendarDate(birthday);
    }

    private void initCalendarDate(String sDate) {
        String[] tempArray = sDate.split("/");
        calendar.set(Calendar.YEAR, Integer.parseInt(tempArray[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(tempArray[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tempArray[0]));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveProfile:
                String[] newFields = getNewTextFields();
                if (!isInputDateValid()) {
                    Toast.makeText(this, R.string.age_too_low, Toast.LENGTH_LONG).show();
                    break;
                }
                btnSave(newFields);
                break;
            case R.id.btnDoNotSaveProfile:
                finish(); // Close current activity and do not save anything
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Check the correctness of the input date
     * @return true if age >= 13
     */
    private boolean isInputDateValid() {
        UserCreation uc = new UserCreation();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.parseInt(uc.getAge(year, month, day)) >= 13;
    }

    private void setEditTextFields(EditText[] fields, String[] params) {
        int idx = 0;
        for (EditText f: fields) {
            f.setText(params[idx]);
            ++idx;
        }
    }

    /**
     * Update the new values to the database
     * @param modCurrent: profile of current user that has been modified
     */
    private void updateDatabaseFields(Musician modCurrent) {
        while(!checkConnection(ProfileModification.this)) { //semi-busy waiting for the connection to be back up
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DataBase db = new DataBase();
        DbAdapter adapter = new DbAdapter(db);
        adapter.update(modCurrent);
        if(videoRecorded) {
            storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), this);
            try {
                storage.upload(videoUri, CloudStorage.FileType.video, mail);
            } catch (IOException e) {
                Toast.makeText(this, R.string.cloud_upload_invalid_file_path, Toast.LENGTH_LONG).show();
            }
        }
        changeStaged = false;
    }

    private void btnSave(String[] newFields) {
        changeStaged = true;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("newFields", newFields);

        Executor mExecutor = Executors.newSingleThreadExecutor();
        AppDatabase localDb = AppDatabase.getInstance(this);
        MusicianDao mdao = localDb.musicianDao();
        String userEmail = CurrentUser.getInstance(this).email;
        //fetches cached user profile
        mExecutor.execute(() -> {
            result = mdao.loadAllByIds(new String[]{userEmail});
        });
        while(result == null) {         //semi-busy waiting for async thread
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                result = new ArrayList<Musician>();
            }
        }
        if(result.isEmpty()){
            Toast.makeText(ProfileModification.this,"Error: couldn't update profile",Toast.LENGTH_LONG);
            finish();
            return;
        }

        Musician currentCachedMusician = result.get(0);
        result = null;
        //update cached profile
        currentCachedMusician.setFirstName(newFields[0]);
        currentCachedMusician.setLastName(newFields[1]);
        currentCachedMusician.setUserName(newFields[2]);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); // KEEP THIS DATE FORMAT !
        MyDate cUserBirthday = currentCachedMusician.getBirthday();
        try {
            Date d = format.parse(newFields[4]);
            cUserBirthday = dateToMyDate(d);

        } catch (ParseException e) {
            Toast.makeText(ProfileModification.this,"Error: couldn't update profile",Toast.LENGTH_LONG);
            finish();
            return;
        }
        currentCachedMusician.setBirthday(cUserBirthday);

        // Upload video to cloud storage
        if(videoRecorded) {
            returnIntent.putExtra("videoUri", videoUri.toString());
        }

        //launches the update to the database on another thread, so that it doesn't hang up the app if not connected to internet
        mExecutor.execute(() -> {
            mdao.updateUsers(new Musician[]{currentCachedMusician});
            updateDatabaseFields(currentCachedMusician);
        });


        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private String[] getNewTextFields() {
        int l = editFields.length;
        String[] newFields = new String[l];
        for (int i = 0; i < editFields.length; i++)
            newFields[i] = editFields[i].getText().toString();
        return newFields;
    }

    /**
     * Helper method to initialize the datepicker dialog
     * @param bdayField
     */
    private void manageDatePickerDialog(EditText bdayField) {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(editFields[4]);
        };
        bdayField.setOnClickListener(v -> new DatePickerDialog(
                ProfileModification.this, date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateLabel(EditText et) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        et.setText(sdf.format(calendar.getTime()));
    }

    public void captureVideo() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, VIDEO_REQUEST);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
            videoRecorded = true;
        }

        showVideo();
    }
}
