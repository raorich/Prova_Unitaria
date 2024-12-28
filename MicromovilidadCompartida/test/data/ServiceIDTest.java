package data;

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
        // Crear instancias de ServiceID con valores alfanuméricos
        validServiceID1 = new ServiceID("S1234");
        validServiceID2 = new ServiceID("S5678");
        // Crear una instancia de ServiceID con UUID generado
        validServiceIDWithUUID = new ServiceID();  // Genera un UUID único
    }

    @Test
    void testValidServiceID() {
        ServiceID id = new ServiceID("S123");
        assertEquals("S123", id.getServiceId());  // Comprobar que el ID asignado es correcto
    }

    @Test
    void testValidUUIDServiceID() {
        // Verificar que el ID generado es un UUID (comprobando longitud y formato)
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
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("12"));  // Too short (menos de 3 caracteres)
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("Service123456789123345678124112313213231321"));  // Too long (más de 36 caracteres)
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("ID@123"));  // Invalid character (caracter no alfanumérico)
    }

    @Test
    void testEqualsAndHashCode() {
        ServiceID id1 = new ServiceID("S123");
        ServiceID id2 = new ServiceID("S123");
        assertEquals(id1, id2);  // Deben ser iguales
        assertEquals(id1.hashCode(), id2.hashCode());  // Los hashCode también deben coincidir
    }

    @Test
    void testToString() {
        ServiceID id = new ServiceID("S123");
        assertEquals("ServiceID{serviceId='S123'}", id.toString());  // La representación en cadena debe ser correcta
    }

    @Test
    void testEquals_SameObject() {
        assertTrue(validServiceID1.equals(validServiceID1));  // Reflexividad: un objeto debe ser igual a sí mismo
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(validServiceID1.equals(null));  // No debe ser igual a null
    }

    @Test
    void testEquals_DifferentClass() {
        String notAServiceID = "NotAServiceID";
        assertFalse(validServiceID1.equals(notAServiceID));  // No debe ser igual a un objeto de una clase diferente
    }

    @Test
    void testEquals_DifferentServiceId() {
        assertFalse(validServiceID1.equals(validServiceID2));  // IDs diferentes, no deben ser iguales
    }

    @Test
    void testEquals_EqualObjects() {
        ServiceID serviceIDDuplicate = new ServiceID("S1234");
        assertTrue(validServiceID1.equals(serviceIDDuplicate));  // Mismos IDs, deben ser iguales
    }

    @Test
    void testUUIDNotEqualToAlphanumeric() {
        // Verificar que un ServiceID con UUID no es igual a uno alfanumérico
        assertFalse(validServiceID1.equals(validServiceIDWithUUID));  // El UUID generado no debe ser igual a un ID alfanumérico
    }
    @Test
    void testUUIDGeneration() {
        // Crear un ServiceID utilizando el constructor sin parámetros
        ServiceID serviceID = new ServiceID();
        String serviceId = serviceID.getServiceId();

        // Verificar que el UUID tiene el formato correcto (36 caracteres alfanuméricos con guiones)
        assertNotNull(serviceId);  // Asegura que no es nulo
        assertTrue(serviceId.matches("[a-f0-9-]{36}"));  // Verifica que el UUID tiene 36 caracteres y el formato correcto

        // Verificar que no hay dos ServiceIDs generados que tengan el mismo ID (comprobación de unicidad)
        ServiceID anotherServiceID = new ServiceID();
        String anotherServiceId = anotherServiceID.getServiceId();

        // Comprobar que los dos IDs generados son diferentes
        assertNotEquals(serviceId, anotherServiceId);
    }
}
