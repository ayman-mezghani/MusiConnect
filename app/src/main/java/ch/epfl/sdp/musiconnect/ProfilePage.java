package ch.epfl.sdp.musiconnect;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ch.epfl.sdp.R;

public class ProfilePage extends StartPage implements View.OnClickListener {

    Button editProfile;
    private ImageView userImgView;
    private TextView laastname, firstname, mail, id;
    private Button signOut;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        editProfile = findViewById(R.id.btnEditProfile);
        editProfile.setOnClickListener(this);

        userImgView = findViewById(R.id.imageView);
        mail = findViewById(R.id.myMail);
        firstname = findViewById(R.id.myFirstname);
        laastname = findViewById(R.id.myLastname);

        //id = findViewById(R.id.txtvId);
        //signOut = findViewById(R.id.btnSignOut);

        //signOut.setOnClickListener(v -> signOut());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            firstname.setText(personName.split(" ")[0]);
            laastname.setText(personName.split(" ")[1]);
            mail.setText(personEmail);
            //id.setText(personId);

            Glide.with(this).load(String.valueOf(personPhoto)).into(userImgView);
        }
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
}
