package micromobility;

import data.*;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PMVehicleTest {

    private PMVehicle vehicle;
    private VehicleID vehicleID;

    @BeforeEach
    public void setUp() {
        vehicleID = new VehicleID("V1234"); // Usamos un ID válido
        vehicle = new PMVehicle(vehicleID); // Creación del vehículo
    }
    @Test
    public void testPMVehicleCreation() {
        // Comprobamos que el vehículo se haya creado correctamente
        assertNotNull(vehicle);
        assertEquals(PMVehicle.PMVState.Available, vehicle.getState()); // El estado inicial debería ser "Available"
        assertEquals(vehicleID, vehicle.getVehicleID()); // Verificamos que el ID del vehículo es el que pasamos
    }
    @Test
    public void testSetNotAvailb_Success() throws ProceduralException {
        vehicle.setNotAvailb(); // Intentamos marcar el vehículo como no disponible
        assertEquals(PMVehicle.PMVState.NotAvailable, vehicle.getState()); // Verificamos que el estado cambió
    }
    @Test
    public void testGetLocation() {
        // Verificamos que la ubicación del vehículo sea null inicialmente
        assertNull(vehicle.getLocation());
    }

    @Test
    public void testSetLocation() {
        GeographicPoint location = new GeographicPoint(40, -74); // Un ejemplo de ubicación (latitud y longitud)
        vehicle.setLocation(location);
        // Verificamos que la ubicación se haya actualizado correctamente
        assertEquals(location, vehicle.getLocation());
    }

    @Test
    public void testSetNotAvailb_FailWhenUnderWay() {
        try {
            vehicle.setNotAvailb();
            vehicle.setUnderWay(); // Primero lo ponemos en uso
            vehicle.setNotAvailb(); // Intentamos marcarlo como no disponible mientras está en uso
            fail("Se esperaba ProceduralException.");
        } catch (ProceduralException e) {
            // Verificamos que la excepción sea la esperada
            assertEquals("No se puede marcar el vehículo como no disponible mientras está en uso.", e.getMessage());
        }
    }

    @Test
    public void testSetUnderWay_Success() throws ProceduralException {
        vehicle.setNotAvailb(); // Primero lo marcamos como no disponible
        vehicle.setUnderWay(); // Luego lo ponemos en marcha
        assertEquals(PMVehicle.PMVState.UnderWay, vehicle.getState()); // Verificamos que el estado cambió correctamente
    }
    @Test
    public void testSetUnderWay_FailWhenAvailable() {
        try {
            vehicle.setUnderWay(); // Intentamos marcar el vehículo como en marcha mientras está disponible
            fail("Se esperaba ProceduralException.");
        } catch (ProceduralException e) {
            // Verificamos que la excepción sea la correcta
            assertEquals("El vehículo no está no esta emparejado.", e.getMessage());
        }
    }
    @Test
    public void testSetAvailb_FailWhenUnderWay() {
        try {
            vehicle.setNotAvailb();
            vehicle.setUnderWay(); // Lo ponemos en marcha
            vehicle.setAvailb(); // Intentamos marcarlo como disponible mientras está en uso
            fail("Se esperaba ProceduralException.");
        } catch (ProceduralException e) {
            // Verificamos que la excepción es la esperada
            assertEquals("No se puede marcar el vehículo como disponible mientras está en uso.", e.getMessage());
        }
    }
}