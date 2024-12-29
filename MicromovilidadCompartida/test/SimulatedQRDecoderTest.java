import static org.junit.jupiter.api.Assertions.*;

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
        String validImageName = "qr_vehicle1.png"; //qr valido de vehiculo1
        VehicleID vehicleID = qrDecoder.getVehicleIDByImg(validImageName);
        assertEquals("V123", vehicleID.getVehicleId());
    }

    @Test
    void testGetVehicleIDByImg_InvalidQRCode() {
        String invalidImageName = "qr_invalid.png";
        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg(invalidImageName));//QR inválido
    }

    @Test
    void testGetVehicleIDByImg_NullImageName() {
        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg(null));//Un nombre de archivo nulo.
    }

    @Test
    void testGetVehicleIDByImg_EmptyImageName() {
        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg(""));//Nombre de archivo vacío
    }

    @Test
    void testCheckPMVAvail_VehicleAvailable() throws PMVNotAvailException {
        VehicleID vehicleID = new VehicleID("V123");
        assertDoesNotThrow(() -> server.checkPMVAvail(vehicleID));//El vehículo debe estar disponible
    }

    @Test
    void testCheckPMVAvail_VehicleNotAvailable() {
        VehicleID unavailableVehicleID = new VehicleID("V999");
        assertThrows(PMVNotAvailException.class, () -> server.checkPMVAvail(unavailableVehicleID));//PMVNotAvailException para un vehículo no disponible
    }

    @Test
    void testBTbroadcast_BluetoothAvailable() {
        SimulatedUnbondedBTSignal btSignal = new SimulatedUnbondedBTSignal(true);
        assertDoesNotThrow(btSignal::BTbroadcast);//Bluetooth debe estar disponible
    }

    @Test
    void testBTbroadcast_BluetoothNotAvailable() {
        SimulatedUnbondedBTSignal btSignal = new SimulatedUnbondedBTSignal(false);
        assertThrows(ConnectException.class, btSignal::BTbroadcast);//ConnectException para Bluetooth no disponible
    }


}
