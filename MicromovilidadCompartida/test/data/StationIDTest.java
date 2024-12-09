package data;

import data.StationID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationIDTest {

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
        StationID id1 = new StationID("Station1");
        StationID id2 = new StationID("Station1");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testToString() {
        StationID id = new StationID("Station1");
        assertEquals("StationID{id='Station1'}", id.toString());
    }
}
