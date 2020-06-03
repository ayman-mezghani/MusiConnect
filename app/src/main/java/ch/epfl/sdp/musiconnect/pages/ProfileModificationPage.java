package ch.epfl.sdp.musiconnect.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.User;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.database.DbSingleton;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static ch.epfl.sdp.musiconnect.ConnectionCheck.checkConnection;
import static ch.epfl.sdp.musiconnect.roomdatabase.MyDateConverter.dateToMyDate;

public class ProfileModificationPage extends ProfilePage implements View.OnClickListener {

    String firstName, lastName, username, mail, birthday, inst, lvl;
    EditText[] editFields;
    final Calendar calendar = Calendar.getInstance();

    protected static int VIDEO_REQUEST = 101;

    private Uri newVideoUri = null;
    private CloudStorage storage;
    private boolean videoRecorded = false;

    private List<Musician> result; //used to fetch from room database
    public static boolean changeStaged = false;    //indicates if there are changes not committed to online database yet

    private Spinner selectedInstrumentSpinner;
    private Spinner selectedLevelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modification);

        editFields = new EditText[]{findViewById(R.id.newFirstName), findViewById(R.id.newLastName),
                findViewById(R.id.newUsername), findViewById(R.id.newEmailAddress), findViewById(R.id.newBirthday)};

        onCreateGetIntentsFields();

        manageDatePickerDialog(editFields[4]);

        setEditTextFields(editFields, new String[]{firstName, lastName, username, mail, birthday});

        findViewById(R.id.btnSaveProfile).setOnClickListener(this);
        findViewById(R.id.btnDoNotSaveProfile).setOnClickListener(this);

        mVideoView = findViewById(R.id.videoViewEdit);
        findViewById(R.id.btnCaptureVideo).setOnClickListener(v -> captureVideo());

        userEmail = CurrentUser.getInstance(this).email;
        getVideoUri(userEmail);

        instrument = findViewById(R.id.editProfileInstrument);
        selectedInstrumentSpinner = findViewById(R.id.editProfileSelectedInstrument);
        // Create an ArrayAdapter using the string array instruments_array and a default spinner layout
        ArrayAdapter<CharSequence> instrumentsAdapter = ArrayAdapter.createFromResource(this, R.array.instruments_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        instrumentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the musicianInstruments spinner
        selectedInstrumentSpinner.setAdapter(instrumentsAdapter);
        selectedInstrumentSpinner.setSelection(instrumentsAdapter.getPosition(this.inst));

        level = findViewById(R.id.editProfileLevel);
        selectedLevelSpinner = findViewById(R.id.editProfileSelectedLevel);
        // Create an ArrayAdapter using the string array levels_array and a default spinner layout
        ArrayAdapter<CharSequence> levelsAdapter = ArrayAdapter.createFromResource(this, R.array.levels_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the musicianLevels spinner
        selectedLevelSpinner.setAdapter(levelsAdapter);
        selectedLevelSpinner.setSelection(levelsAdapter.getPosition(this.lvl));
    }

    @Override
    public void onStart() {
        super.onStart();
        showVideo(newVideoUri == null ? videoUri : newVideoUri);
    }

    private void onCreateGetIntentsFields() {
        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        username = getIntent().getStringExtra("USERNAME");
        mail = getIntent().getStringExtra("MAIL");
        birthday = getIntent().getStringExtra("BIRTHDAY");
        inst = getIntent().getStringExtra("INSTRUMENT");
        lvl = getIntent().getStringExtra("LEVEL");
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
     *
     * @return true if age >= 13
     */
    private boolean isInputDateValid() {
        UserCreationPage uc = new UserCreationPage();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return Integer.parseInt(uc.getAge(year, month, day)) >= 13;
    }

    private void setEditTextFields(EditText[] fields, String[] params) {
        int idx = 0;
        for (EditText f : fields) {
            f.setText(params[idx]);
            ++idx;
        }
    }

    /**
     * Update the new values to the database
     *
     * @param modCurrent: profile of current user that has been modified
     */
    private void updateDatabaseFields(Musician modCurrent) {
        while (!checkConnection(ProfileModificationPage.this)) { //semi-busy waiting for the connection to be back up
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DbAdapter adapter = DbSingleton.getDbInstance();
        adapter.update(DbDataType.Musician, modCurrent);
        if (videoRecorded) {
            storage = CloudStorageSingleton.getCloudInstance(this);
            try {
                storage.upload(CloudStorage.FileType.video, userEmail, newVideoUri);
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
        while (result == null) {         //semi-busy waiting for async thread
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                result = new ArrayList<Musician>();
            }
        }
        if (result.isEmpty()) {
            Toast.makeText(ProfileModificationPage.this, R.string.error_updating_profile, Toast.LENGTH_LONG).show();
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
            Toast.makeText(ProfileModificationPage.this, R.string.error_updating_profile, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        currentCachedMusician.setBirthday(cUserBirthday);

        // Upload video to cloud storage
        if (videoRecorded) {
            Toast.makeText(this, R.string.video_uploading, Toast.LENGTH_SHORT).show();
        }

        currentCachedMusician.setTypeOfUser(CurrentUser.getInstance(this).getTypeOfUser());
        currentCachedMusician.removeAllInstruments();

        String instr = selectedInstrumentSpinner.getSelectedItem().toString();
        String[] instrArray = getResources().getStringArray(R.array.instruments_array);
        String lvl = selectedLevelSpinner.getSelectedItem().toString();
        String[] lvlArray = getResources().getStringArray(R.array.levels_array);

        returnIntent.putExtra("INSTRUMENT", instr);
        returnIntent.putExtra("LEVEL", lvl);

        if (!(lvl.equals(lvlArray[0]) || instr.equals(instrArray[0]))) {
            Instrument i = Instrument.getInstrumentFromValue(instr);
            Level l = Level.getLevelFromValue(lvl);
            currentCachedMusician.addInstrument(i, l);
        }

        //launches the update to the database on another thread, so that it doesn't hang up the app if not connected to internet
        mExecutor.execute(() -> {
            mdao.updateUsers(new Musician[]{currentCachedMusician});
            updateDatabaseFields(currentCachedMusician);
        });
        CurrentUser.getInstance(ProfileModificationPage.this).setMusician(currentCachedMusician);

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
     *
     * @param bdayField
     */
    private void manageDatePickerDialog(EditText bdayField) {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(editFields[4]);
        };

        String[] bday = birthday.split("/");
        calendar.set(Integer.parseInt(bday[2]), Integer.parseInt(bday[1]) - 1, Integer.parseInt(bday[0]));

        bdayField.setOnClickListener(v -> new DatePickerDialog(
                ProfileModificationPage.this, date, calendar.get(Calendar.YEAR),
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
            newVideoUri = data.getData();
            videoRecorded = true;
        }
    }

    @Override
    protected void loadUserProfile(User user) {
    }
}
