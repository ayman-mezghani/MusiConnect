package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Type;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class GoogleLogin extends AppCompatActivity {
    private static String collection = "newtest";

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "Error";
    private static GoogleLogin thisActivity;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signin;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thisActivity = this;
        setContentView(R.layout.activity_google_login);

        signin = findViewById(R.id.sign_in_button);
        signin.setOnClickListener(v -> signIn());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            DbAdapter db = DbGenerator.getDbInstance();
            Context ctx = this;

            db.exists(DbUserType.Musician, account.getEmail(), new DbCallback() {
                @Override
                public void existsCallback(boolean exists) {
                    db.read(DbUserType.Musician, account.getEmail(), new DbCallback() {
                    @Override
                    public void readCallback(User user) {
                        CurrentUser.getInstance(ctx).setTypeOfUser(((Musician) user).getTypeOfUser());
                        StartPage.updateCurrentUserBand(ctx);
                    }
                });

                redirect(exists);
                finish();
                }
            });
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public static void finishActivity() {
        if (thisActivity != null)
            thisActivity.finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            DbAdapter db = DbGenerator.getDbInstance();
            Context ctx = this;
            db.exists(DbUserType.Musician, account.getEmail(), new DbCallback() {
                @Override
                public void existsCallback(boolean exists) {
                    db.read(DbUserType.Musician, account.getEmail(), new DbCallback() {
                        @Override
                        public void readCallback(User user) {
                            CurrentUser.getInstance(ctx).setTypeOfUser(((Musician) user).getTypeOfUser());


                        }
                    });
                    redirect(exists);
                }
            });

            // Signed in successfully

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
        }
    }

    protected void redirect(boolean userExists) {
        if (userExists) {
            CurrentUser.getInstance(GoogleLogin.this).setCreatedFlag();
            startActivity(new Intent(GoogleLogin.this, ch.epfl.sdp.musiconnect.StartPage.class));
            finish();
        } else {
            startActivity(new Intent(GoogleLogin.this, ch.epfl.sdp.musiconnect.UserCreation.class));
        }
    }
}
