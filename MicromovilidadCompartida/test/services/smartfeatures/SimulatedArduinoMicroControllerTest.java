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
    private ServiceID serviceID;
    private VehicleID vehicleID;

    @BeforeEach
    void setUp() {
        vehicleID = new VehicleID("V1234");
        pmVehicle = new PMVehicle(vehicleID);
        userAccount = new UserAccount("user123");
        stationID = new StationID("ST123");
        location = new GeographicPoint(40.7128f, -74.0060f);
        serviceID = new ServiceID("S1234");
        BigDecimal importAmount = new BigDecimal("10.00");

        journeyService = new JourneyService(serviceID, userAccount, importAmount, 'C');

        controller = new SimulatedArduinoMicroController(
                true, // Bluetooth conectado
                true, // Vehículo disponible
                pmVehicle,
                journeyService,
                stationID,
                location,
                userAccount
        );
    }

    @Test
    void testSetBTconnection_Success() {
        assertDoesNotThrow(() -> controller.setBTconnection(),
                "La conexión Bluetooth debe establecerse correctamente.");
    }

    @Test
    void testSetBTconnection_Failure() {
        controller = new SimulatedArduinoMicroController(
                false, true, pmVehicle, journeyService, stationID, location, userAccount);

        assertThrows(ConnectException.class, () -> controller.setBTconnection(),
                "Se esperaba una ConnectException al intentar conectar Bluetooth.");
    }

    @Test
    void testStartDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        // Aseguramos que el vehículo comience en estado 'Available'
        pmVehicle.setNotAvailb();
        controller.setBTconnection();
        controller.startDriving();

        // Verificamos los resultados esperados
        assertEquals(PMVehicle.PMVState.UnderWay, pmVehicle.getState(),
                "El estado del vehículo debe cambiar a 'UnderWay'.");
        assertTrue(journeyService.isInProgress(),
                "El trayecto debe estar marcado como en progreso.");
    }


    @Test
    void testStartDriving_BluetoothNotConnected() {
        controller = new SimulatedArduinoMicroController(
                false, true, pmVehicle, journeyService, stationID, location, userAccount);

        assertThrows(ConnectException.class, () -> controller.startDriving(),
                "Se esperaba una ConnectException cuando Bluetooth no está conectado.");
    }

    @Test
    void testStartDriving_VehicleNotAvailable() {
        controller = new SimulatedArduinoMicroController(
                true, false, pmVehicle, journeyService, stationID, location, userAccount);

        assertThrows(ProceduralException.class, () -> controller.startDriving(),
                "Se esperaba que el vehiculo ha sido previamente emparejado");
    }

    @Test
    void testStartDriving_IncorrectVehicleState() throws ProceduralException {
        pmVehicle.setAvailb();

        assertThrows(ProceduralException.class, () -> controller.startDriving(),
                "Se esperaba una ProceduralException debido al estado incorrecto del vehículo.");
    }

    @Test
    void testStopDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        pmVehicle.setNotAvailb();
        pmVehicle.setUnderWay();
        journeyService.setInProgress(true);

        assertDoesNotThrow(() -> controller.stopDriving(),
                "La detención del trayecto debe realizarse correctamente.");
    }

    @Test
    void testStopDriving_BluetoothNotConnected() throws ProceduralException {
        controller = new SimulatedArduinoMicroController(
                false, true, pmVehicle, journeyService, stationID, location, userAccount);

        pmVehicle.setNotAvailb();
        pmVehicle.setUnderWay();
        journeyService.setInProgress(true);

        assertThrows(ConnectException.class, () -> controller.stopDriving(),
                "Se esperaba una ConnectException cuando Bluetooth no está conectado.");
    }

    @Test
    void testStopDriving_VehicleNotUnderWay() throws ProceduralException {
        pmVehicle.setAvailb();

        assertThrows(ProceduralException.class, () -> controller.stopDriving(),
                "Se esperaba una ProceduralException debido al estado incorrecto del vehículo.");
    }

    @Test
    void testStopDriving_TrayectoNotInProgress() throws ProceduralException {
        pmVehicle.setNotAvailb();
        pmVehicle.setUnderWay();
        journeyService.setInProgress(false);

        assertThrows(ProceduralException.class, () -> controller.stopDriving(),
                "Se esperaba una ProceduralException porque el trayecto no está en progreso.");
    }

    @Test
    void testUndoBTConnection() {
        assertDoesNotThrow(() -> controller.undoBTconnection(),
                "La desconexión Bluetooth debe realizarse sin problemas.");
    }
}
