package ch.epfl.sdp.musiconnect;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.IllegalFormatException;

public class CurrentUser {
    // static variable single_instance of type Singleton
    private static CurrentUser single_instance = null;
    public final String email;

    private boolean createdFlag = false;
    private TypeOfUser type;
    private String bandName;
    private Musician m;
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
    }

    public boolean getCreatedFlag() {
        return createdFlag;
    }

    public TypeOfUser getTypeOfUser() { return this.type; }

    public void setTypeOfUser(TypeOfUser t) { this.type = t; }

    public String getBandName() { return this.bandName; }

    public void setBandName(String bandName) {
        if (this.type == TypeOfUser.Band)
            this.bandName = bandName;
        else
            throw new IllegalArgumentException("You can only set a band name if you are a band");
    }

    public void setMusician(Musician m) {
        this.m = m;
    }

    public Musician getMusician() {
        return this.m;
    }

    public static void flush() {
        single_instance = null;
    }
}
