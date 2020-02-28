package ch.epfl.sdp.musiconnect;

import java.util.Set;

public class Musician {

    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private String emailAddress;
    private String videoURL;
    private Set<Instrument> instruments;


    public void setFirstName(String newFirstName) {
        // TODO : Conditions on the new first name
        firstName = newFirstName;
    }

    public String getFirstName() {
        // TODO : Return a copy of the first name?
        return firstName;
    }

    public void setLastName(String newLastName) {
        // TODO : Conditions on the new last name
        lastName = newLastName;
    }

    public String getLastName() {
        // TODO : Return a copy of the last name?
        return lastName;
    }

    public void setUserName(String newUserName) {
        // TODO : Conditions on the new user name
        userName = newUserName;
    }

    public String getUserName() {
        // TODO : Return a copy of the user name?
        return userName;
    }

    public void setAge(int newAge) {
        // TODO : Conditions on the new age
        age = newAge;
    }

    public int getAge() {
        return age;
    }

    public void setEmailAddress(String newEmailAddress) {
        // TODO : Conditions on the new email address
        emailAddress = newEmailAddress;
    }

    public String getEmailAddress() {
        // TODO : Return a copy of the email address?
        return emailAddress;
    }

    public void setVideoURL(String newVideoURL) {
        // TODO : Conditions on the new video URL
        videoURL = newVideoURL;
    }

    public String getVideoURL() {
        // TODO : Return a copy of the video URL?
        return videoURL;
    }

}
