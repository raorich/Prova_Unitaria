package data;

import data.ServiceID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceIDTest {

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
        ServiceID serviceID = new ServiceID("S1234");
        assertTrue(serviceID.equals(serviceID)); // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        ServiceID serviceID = new ServiceID("S1234");
        assertFalse(serviceID.equals(null)); // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        ServiceID serviceID = new ServiceID("S1234");
        String notAServiceID = "NotAServiceID";
        assertFalse(serviceID.equals(notAServiceID)); // No debe ser igual a un objeto de otra clase
    }

    @Test
    void testEquals_DifferentServiceId() {
        ServiceID serviceID1 = new ServiceID("S1234");
        ServiceID serviceID2 = new ServiceID("S5678");
        assertFalse(serviceID1.equals(serviceID2)); // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        ServiceID serviceID1 = new ServiceID("S1234");
        ServiceID serviceID2 = new ServiceID("S1234");
        assertTrue(serviceID1.equals(serviceID2)); // Mismos IDs, deben ser iguales
    }
}