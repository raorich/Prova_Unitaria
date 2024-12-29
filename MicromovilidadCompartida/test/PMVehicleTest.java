import data.*;
import exceptions.*;
import micromobility.PMVehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PMVehicleTest {

    private PMVehicle vehicle;
    private VehicleID vehicleID;

    @BeforeEach
    public void setUp() {
        vehicleID = new VehicleID("V1234");
        vehicle = new PMVehicle(vehicleID);
    }
    @Test
    public void testPMVehicleCreation() {
        assertNotNull(vehicle);
        assertEquals(PMVehicle.PMVState.Available, vehicle.getState());
        assertEquals(vehicleID, vehicle.getVehicleID()); // Verificamos que el ID del vehículo es el que pasamos
    }
    @Test
    public void testSetNotAvailb_Success() throws ProceduralException {
        vehicle.setNotAvailb();
        assertEquals(PMVehicle.PMVState.NotAvailable, vehicle.getState()); // Verificamos que el estado cambió
    }
    @Test
    public void testGetLocation() {
        assertNull(vehicle.getLocation());
    }

    @Test
    public void testSetLocation() {
        GeographicPoint location = new GeographicPoint(40, -74);
        vehicle.setLocation(location);
        assertEquals(location, vehicle.getLocation());
    }

    @Test
    public void testSetNotAvailb_FailWhenUnderWay() {
        try {
            vehicle.setNotAvailb();
            vehicle.setUnderWay();
            vehicle.setNotAvailb();
            fail("Se esperaba ProceduralException.");
        } catch (ProceduralException e) {
            assertEquals("No se puede marcar el vehículo como no disponible mientras está en uso.", e.getMessage());
        }
    }

    @Test
    public void testSetUnderWay_Success() throws ProceduralException {
        vehicle.setNotAvailb();
        vehicle.setUnderWay();
        assertEquals(PMVehicle.PMVState.UnderWay, vehicle.getState()); // Verificamos que el estado cambió correctamente
    }
    @Test
    public void testSetUnderWay_FailWhenAvailable() {
        try {
            vehicle.setUnderWay();
            fail("Se esperaba ProceduralException.");
        } catch (ProceduralException e) {
            assertEquals("El vehículo no está no esta emparejado.", e.getMessage());
        }
    }
    @Test
    public void testSetAvailb_FailWhenUnderWay() {
        try {
            vehicle.setNotAvailb();
            vehicle.setUnderWay();
            vehicle.setAvailb();
            fail("Se esperaba ProceduralException.");
        } catch (ProceduralException e) {
            assertEquals("No se puede marcar el vehículo como disponible mientras está en uso.", e.getMessage());
        }
    }
}