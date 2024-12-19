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
    } //equals,tostring fer test

    @Test
    void testEquals_SameObject() {
        StationID stationID = new StationID("ABC123");
        assertTrue(stationID.equals(stationID)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        StationID stationID = new StationID("ABC123");
        assertFalse(stationID.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        StationID stationID = new StationID("ABC123");
        String notAStationID = "NotAStationID";
        assertFalse(stationID.equals(notAStationID)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentID() {
        StationID stationID1 = new StationID("ABC123");
        StationID stationID2 = new StationID("DEF456");
        assertFalse(stationID1.equals(stationID2)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        StationID stationID1 = new StationID("ABC123");
        StationID stationID2 = new StationID("ABC123");
        assertTrue(stationID1.equals(stationID2)); // Mismos IDs, deben ser iguales
    }
}
