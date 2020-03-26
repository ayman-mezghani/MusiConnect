package ch.epfl.sdp.musiconnect;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Pellegrini, EPFL
 */
public class LocationClassUnitTests {

    @Test
    public void gettersOfLocationClassWork() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
        assertEquals(location, location.getLocation());
    }

    @Test
    public void setLatitudeWorksWithValidInput() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        double newLatitude = -45.0;

        assertEquals(latitude, location.getLatitude());
        location.setLatitude(newLatitude);
        assertEquals(newLatitude, location.getLatitude());
    }

    @Test
    public void setLatitudeThrowsExceptionIfLatitudeIsInvalid() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        double lowLatitude = -100.0;
        double highLatitude = 100.0;

        assertThrows(IllegalArgumentException.class, () -> location.setLatitude(lowLatitude));
        assertThrows(IllegalArgumentException.class, () -> location.setLatitude(highLatitude));
    }

    @Test
    public void setLongitudeWorksWithValidInput() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        double newLongitude = -145.0;

        assertEquals(longitude, location.getLongitude());
        location.setLongitude(newLongitude);
        assertEquals(newLongitude, location.getLongitude());
    }

    @Test
    public void setLongitudeThrowsExceptionIfLongitudeIsInvalid() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        double lowLongitude = -180.0;
        double highLongitude = 200.0;

        assertThrows(IllegalArgumentException.class, () -> location.setLongitude(lowLongitude));
        assertThrows(IllegalArgumentException.class, () -> location.setLongitude(highLongitude));
    }

    @Test
    public void setLocationWorksWithValidInput() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        double newLatitude = 54.5;
        double newLongitude = -145.0;
        Location newLocation = new Location(newLatitude, newLongitude);

        assertEquals(location, location.getLocation());
        location.setLocation(newLocation);
        assertEquals(newLocation, location.getLocation());
    }

    @Test
    public void equalsOfLocationClassWorksWithSameInstance() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        assertEquals(true, location.equals(location));
    }

    @Test
    public void equalsOfLocationClassWorksWithSameLocation() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        Location thatLocation = new Location(location);

        assertEquals(true, location.equals(thatLocation));
    }

    @Test
    public void equalsOfLocationClassWorksWithDifferentLatitude() {
        double firstLatitude = 0.0;
        double secondLatitude = 50.0;
        double longitude = 0.0;
        Location firstLocation = new Location(firstLatitude, longitude);
        Location secondLocation = new Location(secondLatitude, longitude);

        assertEquals(false, firstLocation.equals(secondLocation));
    }

    @Test
    public void equalsOfLocationClassWorksWithDifferentLongitude() {
        double latitude = 0.0;
        double firstLongitude = 0.0;
        double secondLongitude = 100.0;
        Location firstLocation = new Location(latitude, firstLongitude);
        Location secondLocation = new Location(latitude, secondLongitude);

        assertEquals(false, firstLocation.equals(secondLocation));
    }

    @Test
    public void equalsOfLocationClassWorksWithDifferentLocation() {
        double firstLatitude = 0.0;
        double firstLongitude = 0.0;
        Location firstLocation = new Location(firstLatitude, firstLongitude);

        double secondLatitude = 50.0;
        double secondLongitude = 100.0;
        Location secondLocation = new Location(secondLatitude, secondLongitude);

        assertEquals(false, firstLocation.equals(secondLocation));
    }

    @Test
    public void equalsOfLocationClassWorksWithObjectDifferentFromLocation() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        int year = 2020;
        int month = 2;
        int date = 2;
        int hours = 20;
        int minutes = 20;
        MyDate day = new MyDate(year, month, date, hours, minutes);

        assertEquals(false, location.equals(day));
    }

    @Test
    public void toStringOfLocationClassWorks() {
        double latitude = 0.0;
        double longitude = 0.0;
        Location location = new Location(latitude, longitude);

        String expectedString = "Location: (" + latitude + ", " + longitude + ")\n";

        assertEquals(expectedString, location.toString());
    }

}
