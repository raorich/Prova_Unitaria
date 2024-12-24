package data;

import data.VehicleID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleIDTest {

    private static VehicleID validVehicleID1;
    private static VehicleID validVehicleID2;
    private static VehicleID differentVehicleID;

    @BeforeAll
    static void setUp() {
        validVehicleID1 = new VehicleID("V1234");
        validVehicleID2 = new VehicleID("V1234");
        differentVehicleID = new VehicleID("V5678");
    }

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
        assertEquals(validVehicleID1, validVehicleID2);
        assertEquals(validVehicleID1.hashCode(), validVehicleID2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("VehicleID{vehicleId='V1234'}", validVehicleID1.toString());
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(validVehicleID1.equals(validVehicleID1)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(validVehicleID1.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        String notAVehicleID = "NotAVehicleID";
        assertFalse(validVehicleID1.equals(notAVehicleID)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentVehicleId() {
        assertFalse(validVehicleID1.equals(differentVehicleID)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        assertTrue(validVehicleID1.equals(validVehicleID2)); // Mismos IDs, deben ser iguales
    }
}
