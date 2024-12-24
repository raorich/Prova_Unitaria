package data;

import data.GeographicPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeographicPointTest {

    private static GeographicPoint validPoint1;
    private static GeographicPoint validPoint2;
    private static GeographicPoint validPointDifferentLatitude;
    private static GeographicPoint validPointDifferentLongitude;

    @BeforeAll
    static void setUp() {
        validPoint1 = new GeographicPoint(45.0f, 90.0f);
        validPoint2 = new GeographicPoint(45.0f, 90.0f);
        validPointDifferentLatitude = new GeographicPoint(30.0f, 90.0f);
        validPointDifferentLongitude = new GeographicPoint(45.0f, 80.0f);
    }

    @Test
    void testValidGeographicPoint() {
        assertEquals(45.0f, validPoint1.getLatitude());
        assertEquals(90.0f, validPoint1.getLongitude());
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
        assertEquals(validPoint1, validPoint2);
        assertEquals(validPoint1.hashCode(), validPoint2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("GeographicPoint{latitude=45.0, longitude=90.0}", validPoint1.toString());
    }

    @Test
    void testEqualsMethod_SameObject() {
        assertTrue(validPoint1.equals(validPoint1)); // Reflexividad: debe ser igual a sí mismo
    }

    @Test
    void testEqualsMethod_NullObject() {
        assertFalse(validPoint1.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEqualsMethod_DifferentClass() {
        String notAGeographicPoint = "Not a GeographicPoint";
        assertFalse(validPoint1.equals(notAGeographicPoint)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEqualsMethod_DifferentLatitude() {
        assertFalse(validPoint1.equals(validPointDifferentLatitude)); // Diferentes latitudes, deben ser diferentes
    }

    @Test
    void testEqualsMethod_DifferentLongitude() {
        assertFalse(validPoint1.equals(validPointDifferentLongitude)); // Diferentes longitudes, deben ser diferentes
    }

    @Test
    void testEqualsMethod_EqualObjects() {
        assertTrue(validPoint1.equals(validPoint2)); // Mismas coordenadas, deben ser iguales
    }
}

