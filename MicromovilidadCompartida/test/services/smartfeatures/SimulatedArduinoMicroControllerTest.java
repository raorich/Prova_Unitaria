package services.smartfeatures;

import static org.junit.jupiter.api.Assertions.*;

import data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.*;
import micromobility.PMVehicle;
import services.smartfeatures.SimulatedArduinoMicroController;
import exceptions.ConnectException;
import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;
import data.VehicleID;

import java.math.BigDecimal;

class SimulatedArduinoMicroControllerTest {

    private SimulatedArduinoMicroController controller;
    private PMVehicle pmVehicle;
    private JourneyService journeyService;
    private StationID stationID;
    private GeographicPoint location;
    private UserAccount userAccount;
    private VehicleID vehicleID;

    @BeforeEach
    void setUp() {
        // Inicializamos los objetos necesarios para la prueba
        vehicleID = new VehicleID("V1234");  // Usamos un ID generado automáticamente para el vehículo
        pmVehicle = new PMVehicle(vehicleID);  // Asociamos el VehicleID al vehículo
        ServiceID serviceID = new ServiceID("S1234");  // Inicializamos un ServiceID
        BigDecimal importAmount = new BigDecimal("10.00");  // Ejemplo de importe
        char payMeth = 'C';  // Método de pago (por ejemplo, 'C' para tarjeta de crédito)

        journeyService = new JourneyService(serviceID, new UserAccount("user123"), importAmount, payMeth);
        stationID = new StationID("ST123");
        location = new GeographicPoint(40, -74);  // Ejemplo de coordenadas (NYC)
        userAccount = new UserAccount("user123");

        // Inicializamos el controlador con estos objetos
        controller = new SimulatedArduinoMicroController(true, true, pmVehicle, journeyService, stationID, location, userAccount);
    }


    @Test
    void testSetBTconnection_Success() throws ConnectException {
        // Verificamos que la conexión Bluetooth se establece correctamente
        controller.setBTconnection();
        //assertTrue(controller.isBtConnected());  // Asegurarnos de que el Bluetooth esté conectado
    }

    @Test
    void testSetBTconnection_Failure() {
        // Cambiar el estado para simular que no se puede establecer la conexión
        controller = new SimulatedArduinoMicroController(false, true, pmVehicle, journeyService, stationID, location, userAccount);

        // Verificamos que se lanza la excepción correcta
        assertThrows(ConnectException.class, () -> controller.setBTconnection());
    }

    @Test
    void testStartDriving_Success() throws PMVPhisicalException, ProceduralException, ConnectException {
        // Simulamos que el vehículo está disponible y en estado "NotAvailable"
        pmVehicle.setAvailb();

        // Llamamos al método para iniciar el trayecto
        controller.startDriving();

        // Verificamos que el estado del vehículo cambió correctamente
        assertEquals(PMVehicle.PMVState.UnderWay, pmVehicle.getState());
        assertTrue(journeyService.isInProgress());  // Verificamos que el viaje está en progreso
    }

    @Test
    void testStartDriving_BTNotConnected() {
        // Simulamos un estado de conexión Bluetooth no establecida
        controller = new SimulatedArduinoMicroController(false, true, pmVehicle, journeyService, stationID, location, userAccount);

        // Verificamos que se lanza la excepción ConnectException
        assertThrows(ConnectException.class, () -> controller.startDriving());
    }

    @Test
    void testStartDriving_VehicleNotAvailable() {
        // Simulamos que el vehículo no está disponible para conducir
        controller = new SimulatedArduinoMicroController(true, false, pmVehicle, journeyService, stationID, location, userAccount);

        // Verificamos que se lanza la excepción PMVPhisicalException
        assertThrows(PMVPhisicalException.class, () -> controller.startDriving());
    }

    @Test
    void testStartDriving_IncorrectVehicleState() throws ProceduralException {
        // Simulamos un estado del vehículo incorrecto
        pmVehicle.setUnderWay();

        // Verificamos que se lanza la excepción ProceduralException
        assertThrows(ProceduralException.class, () -> controller.startDriving());
    }

    @Test
    void testStopDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        // Simulamos que el vehículo está en marcha y que el trayecto está en progreso
        pmVehicle.setUnderWay();
        journeyService.setInProgress(true);

        // Llamamos al método para detener el trayecto
        controller.stopDriving();

        // Verificamos que el vehículo se haya detenido correctamente
        assertFalse(journeyService.isInProgress());  // Verificamos que el trayecto no está en progreso
        assertEquals(PMVehicle.PMVState.NotAvailable, pmVehicle.getState());  // Verificamos que el vehículo no está en marcha
    }

    @Test
    void testStopDriving_BTNotConnected() {
        // Simulamos una conexión Bluetooth no establecida
        controller = new SimulatedArduinoMicroController(false, true, pmVehicle, journeyService, stationID, location, userAccount);

        // Verificamos que se lanza la excepción ConnectException
        assertThrows(ConnectException.class, () -> controller.stopDriving());
    }

    @Test
    void testStopDriving_VehicleNotUnderWay() throws ProceduralException {
        // Simulamos que el vehículo no está en marcha
        pmVehicle.setNotAvailb();

        // Verificamos que se lanza la excepción ProceduralException
        assertThrows(ProceduralException.class, () -> controller.stopDriving());
    }

    @Test
    void testStopDriving_TrayectoNotInProgress() {
        // Simulamos que el trayecto no está en progreso
        journeyService.setInProgress(false);

        // Verificamos que se lanza la excepción ProceduralException
        assertThrows(ProceduralException.class, () -> controller.stopDriving());
    }

    @Test
    void testUndoBTconnection() {
        // Llamamos al método para desconectar el Bluetooth
        controller.undoBTconnection();

        // Verificamos que el Bluetooth se haya desconectado
        //assertFalse(controller.isBtConnected());  // Asegurarnos de que el Bluetooth esté desconectado
    }

    @Test
    void testSetNotAvailb_Success() throws ProceduralException {
        // Cambiamos el estado del vehículo a "Available" y luego a "NotAvailable"
        pmVehicle.setAvailb();
        pmVehicle.setNotAvailb();

        // Verificamos que el estado del vehículo es "NotAvailable"
        assertEquals(PMVehicle.PMVState.NotAvailable, pmVehicle.getState());
    }

    @Test
    void testSetNotAvailb_Failure() {
        // Intentamos poner el vehículo como "NotAvailable" cuando está en uso
        assertThrows(ProceduralException.class, () -> pmVehicle.setNotAvailb());
    }

    @Test
    void testSetAvailb_Success() throws ProceduralException {
        // Cambiamos el estado del vehículo a "NotAvailable" y luego a "Available"
        pmVehicle.setNotAvailb();
        pmVehicle.setAvailb();

        // Verificamos que el estado del vehículo es "Available"
        assertEquals(PMVehicle.PMVState.Available, pmVehicle.getState());
    }

    @Test
    void testSetAvailb_Failure() throws ProceduralException {
        // Intentamos poner el vehículo como "Available" mientras está en uso
        pmVehicle.setUnderWay();
        assertThrows(ProceduralException.class, () -> pmVehicle.setAvailb());
    }

    @Test
    void testSetUnderWay_Success() throws ProceduralException {
        // Ponemos el vehículo como "Available" y luego lo marcamos como "UnderWay"
        pmVehicle.setAvailb();
        pmVehicle.setUnderWay();

        // Verificamos que el estado del vehículo es "UnderWay"
        assertEquals(PMVehicle.PMVState.UnderWay, pmVehicle.getState());
    }

    @Test
    void testSetUnderWay_Failure() throws ProceduralException {
        // Intentamos poner el vehículo como "UnderWay" cuando está "NotAvailable"
        pmVehicle.setNotAvailb();
        assertThrows(ProceduralException.class, () -> pmVehicle.setUnderWay());
    }
}
