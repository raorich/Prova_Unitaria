package data;

import data.ServiceID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceIDTest {

    private static ServiceID validServiceID1;
    private static ServiceID validServiceID2;

    @BeforeAll
    static void setUp() {
        validServiceID1 = new ServiceID("S1234");
        validServiceID2 = new ServiceID("S5678");
    }

    @Test
    void testValidServiceID() {
        ServiceID id = new ServiceID("S123");
        assertEquals("S123", id.getServiceId());
    }

    @Test
    void testNullOrEmptyServiceID() {
        assertThrows(IllegalArgumentException.class, () -> new ServiceID(null));
        assertThrows(IllegalArgumentException.class, () -> new ServiceID(""));
    }

    @Test
    void testInvalidServiceID() {
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("12"));  // Too short
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("Service12345"));  // Too long
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("ID@123"));  // Invalid character
    }

    @Test
    void testEqualsAndHashCode() {
        ServiceID id1 = new ServiceID("S123");
        ServiceID id2 = new ServiceID("S123");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testToString() {
        ServiceID id = new ServiceID("S123");
        assertEquals("ServiceID{serviceId='S123'}", id.toString());
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(validServiceID1.equals(validServiceID1)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(validServiceID1.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        String notAServiceID = "NotAServiceID";
        assertFalse(validServiceID1.equals(notAServiceID)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentServiceId() {
        assertFalse(validServiceID1.equals(validServiceID2)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        ServiceID serviceIDDuplicate = new ServiceID("S1234");
        assertTrue(validServiceID1.equals(serviceIDDuplicate)); // Mismos IDs, deben ser iguales
    }
}
