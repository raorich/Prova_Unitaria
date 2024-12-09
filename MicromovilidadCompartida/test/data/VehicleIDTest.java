package data;

import data.VehicleID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleIDTest {

    @Test
    void testValidVehicleID() {
        VehicleID id = new VehicleID("V123");
        assertEquals("V123", id.getVehicleId());
    }

    @Test
    void testNullOrEmptyVehicleID() {
        assertThrows(IllegalArgumentException.class, () -> new VehicleID(null));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID(""));
    }

    @Test
    void testInvalidVehicleID() {
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("12"));  // Too short
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("Vehicle12345"));  // Too long
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("ID@123"));  // Invalid character
    }

    @Test
    void testEqualsAndHashCode() {
        VehicleID id1 = new VehicleID("V123");
        VehicleID id2 = new VehicleID("V123");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testToString() {
        VehicleID id = new VehicleID("V123");
        assertEquals("VehicleID{vehicleId='V123'}", id.toString());
    }
}
