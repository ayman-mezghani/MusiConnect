package ch.epfl.sdp.musiconnect;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

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
    private List<Band> band;
    private GoogleSignInAccount acct;
    private TypeOfUser type;



    // private constructor restricted to this class itself
    private CurrentUser(Context context) {
        if (!checktest()) {
            acct = GoogleSignIn.getLastSignedInAccount(context);
            if (acct != null) {
                email = acct.getEmail();
            } else email = "";
        } else {
            email = "bobminion@gmail.com";
        }
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

    public String getBandName() {
        return this.bandName;
    }

    public void setBandName(String bandName) {
        if (this.musician.getTypeOfUser() == TypeOfUser.Band)
            this.bandName = bandName;
        else
            throw new IllegalArgumentException("You can only set a band name if you are a band");
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

    public void setMusician(Musician m) {
        this.musician = m;
        this.type = m.getTypeOfUser();
    }

    public Musician getMusician() {
        return this.musician;
    }

    public static void flush() {
        single_instance = null;
    }


    public void setLocation(Location location) {
        musician.setLocation(new MyLocation(location.getLatitude(), location.getLongitude()));
    }

    public Location getLocation() {
        Location newLocation = new Location("");
        newLocation.setLatitude(musician.getLocation().getLatitude());
        newLocation.setLongitude(musician.getLocation().getLongitude());
        return newLocation;
    }


    public Band getBand() {
        if(this.getTypeOfUser() == TypeOfUser.Band) return this.band.get(0);
        return null;
    }

    public void setBand(Band b) {
        this.band = new ArrayList<>();
        this.band.add(b);
    }

    public List<Band> getBands() {
        return this.band;
    }

    public void setBands(List<Band> b) {
        if(b != null && b.size() > 0)
            this.band = b;
    }

    public TypeOfUser getTypeOfUser() {
        return this.type;
    }

    public void setTypeOfUser(TypeOfUser t) {
        this.type = t;
    }
}
