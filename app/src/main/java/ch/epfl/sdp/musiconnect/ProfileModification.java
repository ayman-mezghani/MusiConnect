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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.SimplifiedMusician;

public class ProfileModification extends AppCompatActivity implements View.OnClickListener {

    String firstName, lastName, username, mail, birthday;
    EditText[] editFields;
    final Calendar calendar = Calendar.getInstance();

    protected static int VIDEO_REQUEST = 101;
    private String testusername = "testUser";
    protected Uri videoUri = null;
    private VideoView mVideoView;
    private CloudStorage storage;
    private boolean videoRecorded = false;


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

        storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), this);
        String path = testusername + "/" + CloudStorage.FileType.video;
        String saveName = testusername + "_" + CloudStorage.FileType.video;
        try {
            storage.download(path, saveName, fileUri -> {
                videoUri = fileUri;

                mVideoView.setVideoURI(videoUri);
                mVideoView.start();
                mVideoView.setOnCompletionListener(mediaPlayer -> mVideoView.start());
            });
        } catch (IOException e) {
            Toast.makeText(this, "An error occured, please contact support.", Toast.LENGTH_LONG).show();
        }
    }

    private void onCreateGetIntentsFields() {
        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        username = getIntent().getStringExtra("USERNAME");
        mail = getIntent().getStringExtra("MAIL");
        birthday = getIntent().getStringExtra("BIRTHDAY");
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
            default:
                // In case another thing happens, simply close the activity and display an error message
                Toast.makeText(this, getString(R.string.error_in_current_process), Toast.LENGTH_SHORT).show();
                finish();
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
     * @param newFields: new values to write
     */
    private void updateDatabaseFields(String[] newFields) {
        DataBase db = new DataBase();
        DbAdapter adapter = new DbAdapter(db);
        Map<String, Object> data = new HashMap<>();
        String[] keys = {"firstName", "lastName", "username", "email", "birthday"};
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i].equals("birthday")) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); // KEEP THIS DATE FORMAT !
                try {
                    Date d = format.parse(newFields[i]);
                    data.put(keys[i], new Timestamp(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                data.put(keys[i], newFields[i]);
        }
        data.put("location", new GeoPoint(0, 0));

        Musician me = new SimplifiedMusician(data).toMusician();
        adapter.update(me);
    }

    private void btnSave(String[] newFields) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("newFields", newFields);

        // Upload video to cloud storage
        if(videoRecorded) {
            returnIntent.putExtra("videoUri", videoUri.toString());
            storage = new CloudStorage(FirebaseStorage.getInstance().getReference(), this);
            try {
                storage.upload(videoUri, CloudStorage.FileType.video, testusername);
            } catch (IOException e) {
                Toast.makeText(this, R.string.cloud_upload_invalid_file_path, Toast.LENGTH_LONG).show();
            }
        }

        updateDatabaseFields(newFields);
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

        if (videoUri != null) {
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
            mVideoView.setOnCompletionListener(mediaPlayer -> mVideoView.start());
        }
    }

}
