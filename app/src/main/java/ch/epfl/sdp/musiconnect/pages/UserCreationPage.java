package ch.epfl.sdp.musiconnect.pages;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.CurrentUser;
import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;
import ch.epfl.sdp.musiconnect.TypeOfUser;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.database.DbSingleton;

public class UserCreationPage extends Page {
    private static String collection = "newtest";
    private static final int GALLERY_REQUEST_CODE = 123;
    private ImageView profilePicture;
    TextView date;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Calendar calendar;
    protected EditText etFirstName;
    protected EditText etLastName;
    public EditText etUserName;
    protected EditText etMail;
    private RadioGroup rdg;

    private TextView instrument;
    private Spinner selectedInstrument;
    private TextView level;
    private Spinner selectedLevel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

        date = findViewById(R.id.etDate);
        profilePicture = findViewById(R.id.userProfilePicture);
        rdg = findViewById(R.id.rdg);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(UserCreationPage.this,
                    (datePicker, year, month, day) -> {
                        date.setText(day + "/" + (month + 1) + "/" + year + " (" + getAge(year, month, day) + " years)");
                        this.year = year;
                        this.month = month + 1;
                        this.dayOfMonth = day;
                    }, this.year, this.month - 1, this.dayOfMonth);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        profilePicture.setOnClickListener(v -> {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Pick an image"), GALLERY_REQUEST_CODE);
        });

        // signout if user choose cancel
        findViewById(R.id.btnUserCreationCancel).setOnClickListener(v -> signOut());

        findViewById(R.id.btnUserCreationCreate).setOnClickListener(v -> {
            if (checkUserCreationInput()) {
                if (((TextView) findViewById(R.id.etDate)).getText().toString().trim().length() > 0) {

                    String username = etUserName.getText().toString();
                    String firstname = etFirstName.getText().toString();
                    String lastname = etLastName.getText().toString();
                    String email = etMail.getText().toString();
                    MyDate d = new MyDate(year, month, dayOfMonth);

                    RadioButton rdb = findViewById(rdg.getCheckedRadioButtonId());

                    DbAdapter db = DbSingleton.getDbInstance();

                    Musician musician = new Musician(firstname, lastname, username, email, d);
                    musician.setLocation(new MyLocation(0, 0));
                    musician.setTypeOfUser(TypeOfUser.valueOf(rdb.getText().toString()));

                    String instr = selectedInstrument.getSelectedItem().toString();
                    String[] instrArray = getResources().getStringArray(R.array.instruments_array);
                    String lvl = selectedLevel.getSelectedItem().toString();
                    String[] lvlArray = getResources().getStringArray(R.array.levels_array);

                    if (!(lvl.equals(lvlArray[0]) || instr.equals(instrArray[0])))
                        musician.addInstrument(Instrument.getInstrumentFromValue(instr), Level.getLevelFromValue(lvl));

                    db.add(DbDataType.Musician, musician);

                    CurrentUser.getInstance(this).setCreatedFlag();
                    CurrentUser.getInstance(this).setMusician(musician);

                    switch (CurrentUser.getInstance(this).getMusician().getTypeOfUser()) {
                        case Band:
                            StartActivityAndFinish(new Intent(UserCreationPage.this, BandCreationPage.class));
                            break;
                        case Musician:
                            StartActivityAndFinish(new Intent(UserCreationPage.this, StartPage.class));
                            break;
                    }

                    GoogleLoginPage.finishActivity();
                    finish();
                } else {
                    Toast.makeText(this, "Select a date of birth", Toast.LENGTH_LONG).show();
                }
            }
        });

        etFirstName = findViewById(R.id.etFirstname);
        etLastName = findViewById(R.id.etLastName);
        etUserName = findViewById(R.id.etUsername);
        etMail = findViewById(R.id.etMail);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            etFirstName.setText(account.getGivenName());
            etLastName.setText(account.getFamilyName());
            etMail.setText(account.getEmail());
        }

        instrument = findViewById(R.id.newProfileInstrument);
        selectedInstrument = findViewById(R.id.newProfileSelectedInstrument);
        // Create an ArrayAdapter using the string array instruments_array and a default spinner layout
        ArrayAdapter<CharSequence> instrumentsAdapter = ArrayAdapter.createFromResource(this, R.array.instruments_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        instrumentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the musicianInstruments spinner
        selectedInstrument.setAdapter(instrumentsAdapter);

        level = findViewById(R.id.newProfileLevel);
        selectedLevel = findViewById(R.id.newProfileSelectedLevel);
        // Create an ArrayAdapter using the string array levels_array and a default spinner layout
        ArrayAdapter<CharSequence> levelsAdapter = ArrayAdapter.createFromResource(this, R.array.levels_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the musicianLevels spinner
        selectedLevel.setAdapter(levelsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_creation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                Intent helpIntent = new Intent(this, HelpPage.class);
                this.startActivity(helpIntent);
                break;
            case R.id.signout:
                signOut();
                break;
            default:
                displayNotFinishedFunctionalityMessage();
                return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            profilePicture.setImageURI(data.getData());
        }
    }

    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
            age -= 1;

        return String.valueOf(age);
    }

    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public boolean checkUserCreationInput() {
        if (isEmpty(etFirstName)) {
            Toast.makeText(this, "Fill Firstname field", Toast.LENGTH_LONG).show();
            return false;
        }
        if (isEmpty(etLastName)) {
            Toast.makeText(this, "Fill Lastname field", Toast.LENGTH_LONG).show();
            return false;
        }
        if (isEmpty(etUserName)) {
            Toast.makeText(this, "Fill Username field", Toast.LENGTH_LONG).show();
            return false;
        }
        if (isEmpty(etMail)) {
            Toast.makeText(this, "Fill Email field", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    protected void StartActivityAndFinish(Intent i) {
        startActivity(i);
        finish();
    }
}