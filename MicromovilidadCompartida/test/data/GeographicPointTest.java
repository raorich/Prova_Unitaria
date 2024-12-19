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
    @Test
    void testEqualsMethod_SameObject() {
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        assertTrue(point.equals(point)); // Reflexividad: debe ser igual a sí mismo
    }

    @Test
    void testEqualsMethod_NullObject() {
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        assertFalse(point.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEqualsMethod_DifferentClass() {
        GeographicPoint point = new GeographicPoint(45.0f, 90.0f);
        String notAGeographicPoint = "Not a GeographicPoint";
        assertFalse(point.equals(notAGeographicPoint)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEqualsMethod_DifferentLatitude() {
        GeographicPoint point1 = new GeographicPoint(45.0f, 90.0f);
        GeographicPoint point2 = new GeographicPoint(30.0f, 90.0f);
        assertFalse(point1.equals(point2)); // Diferentes latitudes, deben ser diferentes
    }

    @Test
    void testEqualsMethod_DifferentLongitude() {
        GeographicPoint point1 = new GeographicPoint(45.0f, 90.0f);
        GeographicPoint point2 = new GeographicPoint(45.0f, 80.0f);
        assertFalse(point1.equals(point2)); // Diferentes longitudes, deben ser diferentes
    }

    @Test
    void testEqualsMethod_EqualObjects() {
        GeographicPoint point1 = new GeographicPoint(45.0f, 90.0f);
        GeographicPoint point2 = new GeographicPoint(45.0f, 90.0f);
        assertTrue(point1.equals(point2)); // Mismas coordenadas, deben ser iguales
    }

}
