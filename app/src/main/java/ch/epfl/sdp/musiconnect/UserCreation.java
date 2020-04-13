package ch.epfl.sdp.musiconnect;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;
import java.util.HashMap;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

public class UserCreation extends Page {
    //public static Musician mainUser;
    private static String collection = "newtest";
    private static final int GALLERY_REQUEST_CODE = 123;
    private ImageView profilePicture;
    TextView date;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Calendar calendar;
    protected EditText etFirstName, etLastName, etUserName, etMail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

        date = findViewById(R.id.etDate);
        profilePicture = findViewById(R.id.userProfilePicture);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(UserCreation.this,
                    (datePicker, year, month, day) -> {
                        date.setText(day + "/" + (month + 1) + "/" + year + " (" + getAge(year, month, day) + " years)");
                        this.year = year;
                        this.month = month + 1;
                        this.dayOfMonth = day;
                    }, year, month, dayOfMonth);
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
/*<<<<<<< HEAD
                    // TODO: Insert Data in database properly (MyDate specifically)
                    mainUser = new Musician(etFirstName.getText().toString(),etLastName.getText().toString(),
                            etUserName.getText().toString(),etMail.getText().toString(), new MyDate(1990,1,1));
                    DataBase db = new DataBase();
                    DbAdapter Adb = new DbAdapter(db);
                    Adb.add(mainUser);
=======*/
                    // TODO: Insert Data in database

                    String username = etUserName.getText().toString();
                    String firstname = etFirstName.getText().toString();
                    String lastname = etLastName.getText().toString();
                    String email = etMail.getText().toString();
                    MyDate d = new MyDate(year, month, dayOfMonth);

                    Musician musician = new Musician(firstname, lastname, username, email, d);
                    musician.setLocation(new MyLocation(0, 0));

                    DbAdapter db = new DbAdapter(new DataBase());
                    db.add(collection, musician);

                    CurrentUser.getInstance(this).setCreatedFlag();


                    StartActivityAndFinish(new Intent(UserCreation.this, StartPage.class));
                    GoogleLogin.finishActivity();
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

    private void StartActivityAndFinish(Intent i) {
        startActivity(i);
        finish();
    }
}