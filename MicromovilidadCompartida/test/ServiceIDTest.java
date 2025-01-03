import data.ServiceID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceIDTest {

    private static ServiceID validServiceID1;
    private static ServiceID validServiceID2;
    private static ServiceID validServiceIDWithUUID;

    @BeforeAll
    static void setUp() {
        validServiceID1 = new ServiceID("S1234");
        validServiceID2 = new ServiceID("S5678");
        validServiceIDWithUUID = new ServiceID();  // Genera un UUID único
    }

    @Test
    void testValidServiceID() {
        ServiceID id = new ServiceID("S123");
        assertEquals("S123", id.getServiceId());
    }

    @Test
    void testValidUUIDServiceID() {
        String serviceId = validServiceIDWithUUID.getServiceId();
        assertTrue(serviceId.matches("[a-f0-9-]{36}"));  // Verifica que el UUID tiene 36 caracteres y el formato correcto
    }

    @Test
    void testNullOrEmptyServiceID() {
        assertThrows(IllegalArgumentException.class, () -> new ServiceID(null));  // No debe permitir null
        assertThrows(IllegalArgumentException.class, () -> new ServiceID(""));    // No debe permitir cadena vacía
    }

    @Test
    void testInvalidServiceID() {
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("12"));  // Too short
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("Service123456789123345678124112313213231321"));  // Too long
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
        assertTrue(validServiceID1.equals(validServiceID1));  // Reflexividad
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(validServiceID1.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        String notAServiceID = "NotAServiceID";
        assertFalse(validServiceID1.equals(notAServiceID));  // No debe ser igual a un objeto de una clase diferente
    }

    @Test
    void testEquals_DifferentServiceId() {
        assertFalse(validServiceID1.equals(validServiceID2));  // IDs diferentes
    }

    @Test
    void testEquals_EqualObjects() {
        ServiceID serviceIDDuplicate = new ServiceID("S1234");
        assertTrue(validServiceID1.equals(serviceIDDuplicate));  // Mismos IDs
    }

    @Test
    void testUUIDNotEqualToAlphanumeric() {
        assertFalse(validServiceID1.equals(validServiceIDWithUUID));  // El UUID generado no debe ser igual a un ID alfanumérico
    }
    @Test
    void testUUIDGeneration() {
        ServiceID serviceID = new ServiceID();
        String serviceId = serviceID.getServiceId();

        assertNotNull(serviceId);
        assertTrue(serviceId.matches("[a-f0-9-]{36}"));  // Verifica que el UUID tiene 36 caracteres y el formato correcto

        ServiceID anotherServiceID = new ServiceID();
        String anotherServiceId = anotherServiceID.getServiceId();

        assertNotEquals(serviceId, anotherServiceId);// Comprobar que los dos IDs generados son diferentes
    }
}
