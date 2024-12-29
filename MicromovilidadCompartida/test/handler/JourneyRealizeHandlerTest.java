package handler;

import data.VehicleID;
import exceptions.ConnectException;
import exceptions.CorruptedImgException;
import exceptions.PMVNotAvailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ServerSimulated;
import services.smartfeatures.SimulatedQRDecoder;
import services.smartfeatures.SimulatedUnbondedBTSignal;

import static org.junit.jupiter.api.Assertions.*;

class SimulatedQRDecoderTest {

    private SimulatedQRDecoder qrDecoder;
    private ServerSimulated server;

    @BeforeEach
    void setUp() {
        qrDecoder = new SimulatedQRDecoder();
        server = new ServerSimulated();
    }

    @Test
    void testGetVehicleIDByImg_ValidQRCode() throws CorruptedImgException {
        String validImageName = "qr_vehicle1.png";
        VehicleID vehicleID = qrDecoder.getVehicleIDByImg(validImageName);
        assertEquals("V123", vehicleID.getVehicleId(), "El ID del vehículo debe ser V123.");
    }

    @Test
    void testGetVehicleIDByImg_InvalidQRCode() {
        String invalidImageName = "qr_invalid.png";
        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg(invalidImageName),
                "Se esperaba CorruptedImgException para un QR inválido.");
    }

    @Test
    void testGetVehicleIDByImg_NullImageName() {
        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg(null),
                "Se esperaba CorruptedImgException para un nombre de archivo nulo.");
    }

    @Test
    void testGetVehicleIDByImg_EmptyImageName() {
        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg(""),
                "Se esperaba CorruptedImgException para un nombre de archivo vacío.");
    }

    @Test
    void testCheckPMVAvail_VehicleAvailable() throws PMVNotAvailException {
        VehicleID vehicleID = new VehicleID("V123");
        assertDoesNotThrow(() -> server.checkPMVAvail(vehicleID), "El vehículo debe estar disponible.");
    }

    @Test
    void testCheckPMVAvail_VehicleNotAvailable() {
        VehicleID unavailableVehicleID = new VehicleID("V999");
        assertThrows(PMVNotAvailException.class, () -> server.checkPMVAvail(unavailableVehicleID),
                "Se esperaba PMVNotAvailException para un vehículo no disponible.");
    }

    @Test
    void testBTbroadcast_BluetoothAvailable() {
        SimulatedUnbondedBTSignal btSignal = new SimulatedUnbondedBTSignal(true);
        assertDoesNotThrow(btSignal::BTbroadcast, "Bluetooth debe estar disponible.");
    }

    @Test
    void testBTbroadcast_BluetoothNotAvailable() {
        SimulatedUnbondedBTSignal btSignal = new SimulatedUnbondedBTSignal(false);
        assertThrows(ConnectException.class, btSignal::BTbroadcast, "Se esperaba ConnectException para Bluetooth no disponible.");
    }


}
