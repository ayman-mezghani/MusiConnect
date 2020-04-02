package ch.epfl.sdp.musiconnect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;
import java.util.HashMap;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DataBase;
import ch.epfl.sdp.musiconnect.database.DbAdapter;

public class UserCreation extends Page {
    public static Musician mainUser;
    private static final int GALLERY_REQUEST_CODE = 123;
    private ImageView profilePicture;
    TextView date;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    protected EditText etFirstName, etLastName, etUserName, etMail;

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

        date.setOnClickListener(v ->{
            datePickerDialog = new DatePickerDialog(UserCreation.this,
                    (datePicker, year, month, day) -> date.setText(day + "/" + (month + 1) + "/" + year + " (" + getAge(year, month, day) + " years)"), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        profilePicture.setOnClickListener(v ->{
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Pick an image"), GALLERY_REQUEST_CODE);
        });

        // signout if user choose cancel
        findViewById(R.id.btnUserCreationCancel).setOnClickListener(v -> signOut());


        etFirstName = findViewById(R.id.etFirstname);
        etLastName = findViewById(R.id.etLastName);
        etUserName = findViewById(R.id.etUsername);
        etMail = findViewById(R.id.etMail);

        findViewById(R.id.btnUserCreationCreate).setOnClickListener(v -> {
            if(checkUserCreationInput()) {
                if (((TextView) findViewById(R.id.etDate)).getText().toString().trim().length() > 0) {
                    // TODO: Insert Data in database properly (MyDate specifically)
                    mainUser = new Musician(etFirstName.getText().toString(),etLastName.getText().toString(),
                            etUserName.getText().toString(),etMail.getText().toString(), new MyDate(1990,1,1));
                    DataBase db = new DataBase();
                    DbAdapter Adb = new DbAdapter(db);
                    Adb.add(mainUser);
                    StartActivityAndFinish(new Intent(UserCreation.this, StartPage.class));
                }
                else {
                    Toast.makeText(this, "Select a date of birth", Toast.LENGTH_LONG).show();
                }
            }
        });
        


        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null){
            etFirstName.setText(account.getGivenName());
            etLastName.setText(account.getFamilyName());
            etMail.setText(account.getEmail());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            profilePicture.setImageURI(data.getData());
        }
    }

    public String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return String.valueOf(age);
    }

    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public boolean checkUserCreationInput() {
        if(isEmpty(etFirstName)){
            Toast.makeText(this, "Fill Firstname field", Toast.LENGTH_LONG).show();
            return false;
        }
        if(isEmpty(etLastName)){
            Toast.makeText(this, "Fill Lastname field", Toast.LENGTH_LONG).show();
            return false;
        }
        if(isEmpty(etUserName)){
            Toast.makeText(this, "Fill Username field", Toast.LENGTH_LONG).show();
            return false;
        }
        if(isEmpty(etMail)){
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