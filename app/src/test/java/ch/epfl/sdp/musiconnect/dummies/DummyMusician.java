package ch.epfl.sdp.musiconnect.dummies;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.musiconnect.Musician;
import ch.epfl.sdp.musiconnect.MyDate;
import ch.epfl.sdp.musiconnect.MyLocation;

/**
 * Class containing hard coded musicians for testing purposes
 */
public class DummyMusician {

    private List<Musician> musicians;


    public DummyMusician() {
        musicians = new ArrayList<>();

        Musician m1 = new Musician("Peter", "Alpha", "PAlpha", "palpha@gmail.com", new MyDate(1990, 10, 25));
        m1.setLocation(new MyLocation(46.52, 6.52));

        Musician m2 = new Musician("Alice", "Bardon", "Alyx", "alyx92@gmail.com", new MyDate(1992, 9, 20));
        m2.setLocation(new MyLocation(46.51, 6.45));

        Musician m3 = new Musician("Carson", "Calme", "CallmeCarson", "callmecarson41@gmail.com", new MyDate(1995, 4, 1));
        m3.setLocation(new MyLocation(46.519, 6.57));

        musicians.add(m1);
        musicians.add(m2);
        musicians.add(m3);

    }

    public int getListSize() {
        return musicians.size();
    }

    public Musician getMusician(int index) {
        if (index < 0 || index >= musicians.size()) {
            return null;
        }
        return musicians.get(index);
    }
}
