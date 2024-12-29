import static org.junit.jupiter.api.Assertions.*;

import data.*;
import micromobility.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertDoesNotThrow(() -> controller.setBTconnection());//Bluetooth debe establecerse correctamente
    }

    @Test
    void testSetBTconnection_Failure() {
        controller = new SimulatedArduinoMicroController(
                false, true, pmVehicle, journeyService, stationID, location, userAccount);

        assertThrows(ConnectException.class, () -> controller.setBTconnection());//ConnectException al intentar conectar Bluetooth
    }

    @Test
    void testStartDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        pmVehicle.setNotAvailb();
        controller.setBTconnection();
        controller.startDriving();

        assertEquals(PMVehicle.PMVState.UnderWay, pmVehicle.getState());//El estado del vehículo debe cambiar a UnderWay
        assertTrue(journeyService.isInProgress());//El trayecto debe estar marcado como en progreso
    }


    @Test
    void testStartDriving_BluetoothNotConnected() {
        controller = new SimulatedArduinoMicroController(
                false, true, pmVehicle, journeyService, stationID, location, userAccount);

        assertThrows(ConnectException.class, () -> controller.startDriving());//ConnectException cuando Bluetooth no está conectado
    }

    @Test
    void testStartDriving_VehicleNotAvailable() {
        controller = new SimulatedArduinoMicroController(
                true, false, pmVehicle, journeyService, stationID, location, userAccount);

        assertThrows(ProceduralException.class, () -> controller.startDriving());
    }

    @Test
    void testStartDriving_IncorrectVehicleState() throws ProceduralException {
        pmVehicle.setAvailb();

        assertThrows(ProceduralException.class, () -> controller.startDriving());//ProceduralException debido al estado incorrecto del vehículo
    }

    @Test
    void testStopDriving_Success() throws ConnectException, PMVPhisicalException, ProceduralException {
        pmVehicle.setNotAvailb();
        pmVehicle.setUnderWay();
        journeyService.setInProgress(true);

        assertDoesNotThrow(() -> controller.stopDriving());//La detención del trayecto
    }

    @Test
    void testStopDriving_BluetoothNotConnected() throws ProceduralException {
        controller = new SimulatedArduinoMicroController(
                false, true, pmVehicle, journeyService, stationID, location, userAccount);

        pmVehicle.setNotAvailb();
        pmVehicle.setUnderWay();
        journeyService.setInProgress(true);

        assertThrows(ConnectException.class, () -> controller.stopDriving());//ConnectException cuando Bluetooth no está conectado
    }

    @Test
    void testStopDriving_VehicleNotUnderWay() throws ProceduralException {
        pmVehicle.setAvailb();

        assertThrows(ProceduralException.class, () -> controller.stopDriving());//ProceduralException debido al estado incorrecto del vehículo
    }

    @Test
    void testStopDriving_TrayectoNotInProgress() throws ProceduralException {
        pmVehicle.setNotAvailb();
        pmVehicle.setUnderWay();
        journeyService.setInProgress(false);

        assertThrows(ProceduralException.class, () -> controller.stopDriving());//ProceduralException porque el trayecto no está en progreso
    }

    @Test
    void testUndoBTConnection() {
        assertDoesNotThrow(() -> controller.undoBTconnection());//Bluetooth debe realizarse sin problemas
    }
}
