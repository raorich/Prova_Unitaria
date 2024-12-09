package data;

import data.GeographicPoint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeographicPointTest {

    @Test
    void testValidGeographicPoint() {
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        assertEquals(45.0f, point.getLatitude());
        assertEquals(90.0f, point.getLongitude());
    }

    @Test
    void testInvalidLatitude() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(-100.0f, 50.0f));
        assertEquals("Latitude must be in range [-90, 90]", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(100.0f, 50.0f));
        assertEquals("Latitude must be in range [-90, 90]", exception.getMessage());
    }

    @Test
    void testInvalidLongitude() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(45.0f, -200.0f));
        assertEquals("Longitude must be in range [-180, 180]", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(45.0f, 200.0f));
        assertEquals("Longitude must be in range [-180, 180]", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        GeographicPoint point1 = new GeographicPoint(45.0f, 90.0f);
        GeographicPoint point2 = new GeographicPoint(45.0f, 90.0f);
        assertEquals(point1, point2);
        assertEquals(point1.hashCode(), point2.hashCode());
    }

    @Test
    void testToString() {
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        assertEquals("GeographicPoint{latitude=45.0, longitude=90.0}", point.toString());
    }
}
