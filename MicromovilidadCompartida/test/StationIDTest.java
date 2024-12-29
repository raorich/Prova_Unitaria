import data.StationID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationIDTest {

    private static StationID validStationID1;
    private static StationID validStationID2;
    private static StationID differentStationID;

    @BeforeAll
    static void setUp() {
        validStationID1 = new StationID("ABC123");
        validStationID2 = new StationID("ABC123");
        differentStationID = new StationID("DEF456");
    }

    @Test
    void testValidStationID() {
        StationID id = new StationID("A123");
        assertEquals("A123", id.getId());
    }

    @Test
    void testNullOrEmptyStationID() {
        assertThrows(IllegalArgumentException.class, () -> new StationID(null));
        assertThrows(IllegalArgumentException.class, () -> new StationID(""));
    }

    @Test
    void testInvalidStationID() {
        assertThrows(IllegalArgumentException.class, () -> new StationID("12"));  // Too short
        assertThrows(IllegalArgumentException.class, () -> new StationID("Station12345"));  // Too long
        assertThrows(IllegalArgumentException.class, () -> new StationID("ID@123"));  // Invalid character
    }

    @Test
    void testEqualsAndHashCode() {
        assertEquals(validStationID1, validStationID2);
        assertEquals(validStationID1.hashCode(), validStationID2.hashCode());
    }
    @Test
    void testToString() {
        assertEquals("StationID{id='ABC123'}", validStationID1.toString());
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(validStationID1.equals(validStationID1)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(validStationID1.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        String notAStationID = "NotAStationID";
        assertFalse(validStationID1.equals(notAStationID)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentID() {
        assertFalse(validStationID1.equals(differentStationID)); // IDs diferentes
    }

    @Test
    void testEquals_EqualObjects() {
        assertTrue(validStationID1.equals(validStationID2)); // Mismos IDs
    }
}
