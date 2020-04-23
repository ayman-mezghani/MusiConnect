package ch.epfl.sdp.musiconnect;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbGenerator;
import ch.epfl.sdp.musiconnect.database.DbUserType;

public class CurrentUser {
    // static variable single_instance of type Singleton
    private static CurrentUser single_instance = null;
    public final String email;

    private boolean createdFlag = false;
    private String bandName;
    private Musician musician;
    private GoogleSignInAccount acct;

    // private constructor restricted to this class itself
    private CurrentUser(Context context) {
        acct = GoogleSignIn.getLastSignedInAccount(context);
        if (acct != null) {
            email = acct.getEmail();
        } else email = "";
    }

    // static method to create instance of Singleton class
    public static CurrentUser getInstance(Context context) {
        if (single_instance == null)
            single_instance = new CurrentUser(context);

        return single_instance;
    }

    public void setCreatedFlag() {
        this.createdFlag = true;
        DbGenerator.getDbInstance().read(DbUserType.Musician, email, new DbCallback() {
            @Override
            public void readCallback(User user) {
                musician = (Musician) user;
            }
        });
    }

    public boolean getCreatedFlag() {
        return createdFlag;
    }

    public String getBandName() { return this.bandName; }

    public void setBandName(String bandName) {
        if (this.musician.getTypeOfUser() == TypeOfUser.Band)
            this.bandName = bandName;
        else
            throw new IllegalArgumentException("You can only set a band name if you are a band");
    }

    public void setMusician(Musician m) {
        this.musician = m;
    }

    public Musician getMusician() {
        return this.musician;
    }

    public static void flush() {
        single_instance = null;
    }
}
