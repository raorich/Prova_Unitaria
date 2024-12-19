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
    @Test
    void testEquals_SameObject() {
        VehicleID vehicleID = new VehicleID("V1234");
        assertTrue(vehicleID.equals(vehicleID)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        VehicleID vehicleID = new VehicleID("V1234");
        assertFalse(vehicleID.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        VehicleID vehicleID = new VehicleID("V1234");
        String notAVehicleID = "NotAVehicleID";
        assertFalse(vehicleID.equals(notAVehicleID)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentVehicleId() {
        VehicleID vehicleID1 = new VehicleID("V1234");
        VehicleID vehicleID2 = new VehicleID("V5678");
        assertFalse(vehicleID1.equals(vehicleID2)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        VehicleID vehicleID1 = new VehicleID("V1234");
        VehicleID vehicleID2 = new VehicleID("V1234");
        assertTrue(vehicleID1.equals(vehicleID2)); // Mismos IDs, deben ser iguales
    }
}
