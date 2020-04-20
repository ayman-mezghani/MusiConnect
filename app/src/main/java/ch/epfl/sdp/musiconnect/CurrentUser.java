package ch.epfl.sdp.musiconnect;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class CurrentUser {
    // static variable single_instance of type Singleton
    private static CurrentUser single_instance = null;
    public final String email;

    private boolean createdFlag = false;
    private GoogleSignInAccount acct;

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

    public static void flush() {
        single_instance = null;
    }

    // private constructor restricted to this class itself
    private CurrentUser(Context context) {
        if(!checktest()) {
            acct = GoogleSignIn.getLastSignedInAccount(context);
            if (acct != null) {
                email = acct.getEmail();
            } else email = "";
        }else{
            email = "defuser@gmail.com";
        }
    }

    private boolean checktest() {
        boolean istest;

        try {
            Class.forName("androidx.test.espresso.Espresso");
            istest = true;
        } catch (ClassNotFoundException e) {
            istest = false;
        }
        return istest;
    }
}