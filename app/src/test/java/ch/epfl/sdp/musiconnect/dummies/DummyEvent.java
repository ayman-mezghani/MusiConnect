package ch.epfl.sdp.musiconnect.dummies;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.musiconnect.Event;
import ch.epfl.sdp.musiconnect.MyDate;

public class DummyEvent {
    private List<Event> events;
    private DummyMusician dm;

    public DummyEvent() {
        dm = new DummyMusician();
        events = new ArrayList<>();

        Event e1 = new Event(dm.getMusician(1), 0);

        e1.setAddress("Westminster, London, England");
        e1.setLocation(51.5007, 0.1245);
        e1.setDateTime(new MyDate(2020, 9, 21, 14, 30));
        e1.setTitle("Event at Big Ben!");
        e1.setMessage("Playing at Big Ben, come watch us play!");


        events.add(e1);

    }

    public int getListSize() {
        return events.size();
    }

    public Event getEvent(int index) {
        return events.get(index);
    }
}
