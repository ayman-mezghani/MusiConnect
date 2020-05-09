package ch.epfl.sdp.musiconnect;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * @author Manuel Pellegrini, EPFL
 */
public abstract class Person extends User {

    private String firstName;
    private String lastName;
    private String userName;
    @PrimaryKey
    @NonNull
    private String emailAddress;
    private MyDate birthday;

    private static final int MAX_FIRST_NAME_LENGTH = 16;
    private static final int MAX_LAST_NAME_LENGTH = 16;
    private static final int MAX_USER_NAME_LENGTH = 16;
    private static final int MAX_EMAIL_ADDRESS_LENGTH = 64;
    private static final int MIN_AGE = 13;
    private static final int MAX_AGE = 120;

    public Person(String firstName, String lastName, String userName, String emailAddress, MyDate birthday) {
        super();
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
        setEmailAddress(emailAddress);
        setBirthday(birthday);
        events = new ArrayList<>();
    }


    public void setFirstName(String firstName) {
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        } else if (firstName.length() > MAX_FIRST_NAME_LENGTH) {
            throw new IllegalArgumentException("First name too long");
        }

        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        } else if (lastName.length() > MAX_LAST_NAME_LENGTH) {
            throw new IllegalArgumentException("Last name too long");
        }

        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserName(String userName) {
        if (userName.isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        } else if (userName.length() > MAX_USER_NAME_LENGTH) {
            throw new IllegalArgumentException("User name too long");
        }

        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress.isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be empty");
        } else if (emailAddress.length() > MAX_EMAIL_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("Email address too long");
        } else if (!(emailAddress.endsWith("@gmail.com"))) {
            throw new IllegalArgumentException("Email address must end with \"@gmail.com\"");
        }

        this.emailAddress = emailAddress;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    private int computeAge(MyDate birthday) {
        MyDate currentDate = new MyDate();

        int currentAge = currentDate.getYear() - birthday.getYear();
        if (currentDate.getMonth() < birthday.getMonth() || currentDate.getMonth() == birthday.getMonth() && currentDate.getDate() < birthday.getDate()) {
            --currentAge;
        }

        return currentAge;
    }

    public void setBirthday(MyDate birthday) {
        MyDate currentDate = new MyDate();

        if (birthday.after(currentDate)) {
            throw new IllegalArgumentException("Birthday has not happened yet");
        }

        int currentAge = computeAge(birthday);

        if (currentAge < MIN_AGE) {
            throw new IllegalArgumentException("Age too low");
        } else if (currentAge > MAX_AGE) {
            throw new IllegalArgumentException("Age too high");
        }

        this.birthday = new MyDate(birthday.getYear(), birthday.getMonth(), birthday.getDate());
    }

    public MyDate getBirthday() {
        return new MyDate(birthday);
    }

    public int getAge() {
        return computeAge(birthday);
    }

    @Override
    public String getName() {
        return userName;
    }
}
