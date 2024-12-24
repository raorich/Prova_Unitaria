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
    private SimulatedUnbondedBTSignal btSignal;
    @BeforeEach
    void setUp() {
        // Inicializamos el QRDecoder
        qrDecoder = new SimulatedQRDecoder();
        server = new ServerSimulated();
        btSignal = new SimulatedUnbondedBTSignal(true); // Bluetooth disponible
    }

    // Caso válido: QR válido "qr_vehicle1.png" debería devolver el ID "V123"
    @Test
    void testGetVehicleIDByImg_ValidQRCode() throws CorruptedImgException {
        // Simulamos la imagen "qr_vehicle1.png"
        String validImageName = "qr_vehicle1.png";

        // Llamamos al método getVehicleIDByImg
        VehicleID vehicleID = qrDecoder.getVehicleIDByImg(validImageName);

        // Comprobamos que el ID del vehículo devuelto sea "V123"
        assertEquals("V123", vehicleID.getVehicleId(), "El ID del vehículo debe ser V123 para qr_vehicle1.png");
    }

    // Caso inválido: QR inválido "qr_invalid.png" debería lanzar CorruptedImgException
    @Test
    void testGetVehicleIDByImg_InvalidQRCode() {
        // Simulamos la imagen inválida "qr_invalid.png"
        String invalidImageName = "qr_invalid.png";

        // Verificamos que se lance la excepción CorruptedImgException
        assertThrows(CorruptedImgException.class, () -> {
            qrDecoder.getVehicleIDByImg(invalidImageName);
        }, "Se esperaba que se lanzara una excepción CorruptedImgException para un QR inválido.");
    }

    // Caso de imagen nula: Debe lanzar CorruptedImgException si se pasa null
    @Test
    void testGetVehicleIDByImg_NullImageName() {
        // Pasamos un nombre de archivo nulo
        String nullImageName = null;

        // Verificamos que se lance la excepción CorruptedImgException
        assertThrows(CorruptedImgException.class, () -> {
            qrDecoder.getVehicleIDByImg(nullImageName);
        }, "Se esperaba que se lanzara una excepción CorruptedImgException para nombre de archivo nulo.");
    }

    // Caso de nombre vacío: Debe lanzar CorruptedImgException si se pasa un nombre vacío
    @Test
    void testGetVehicleIDByImg_EmptyImageName() {
        // Pasamos un nombre de archivo vacío
        String emptyImageName = "";

        // Verificamos que se lance la excepción CorruptedImgException
        assertThrows(CorruptedImgException.class, () -> {
            qrDecoder.getVehicleIDByImg(emptyImageName);
        }, "Se esperaba que se lanzara una excepción CorruptedImgException para nombre de archivo vacío.");
    }

    // Nuevo test: Verificar si el vehículo está disponible en el servidor
    @Test
    void testCheckPMVAvail_VehicleAvailable() throws PMVNotAvailException {
        // Simulamos un ID de vehículo válido
        VehicleID vehicleID = new VehicleID("V123");

        // Llamamos al método checkPMVAvail del servidor
        assertDoesNotThrow(() -> server.checkPMVAvail(vehicleID),
                "El vehículo debe estar disponible y no se debe lanzar excepción.");
    }

    // Nuevo test: Verificar si el vehículo no está disponible en el servidor
    @Test
    void testCheckPMVAvail_VehicleNotAvailable() {
        // Simulamos un ID de vehículo no disponible
        VehicleID unavailableVehicleID = new VehicleID("V999");

        // Verificamos que se lance una excepción PMVNotAvailException
        assertThrows(PMVNotAvailException.class, () -> server.checkPMVAvail(unavailableVehicleID),
                "Se esperaba una excepción PMVNotAvailException para un vehículo no disponible.");
    }

    // Nuevo test: Verificar conexión Bluetooth disponible
    @Test
    void testBTbroadcast_BluetoothAvailable() {
        // Llamamos al método BTbroadcast y verificamos que no lanza excepciones
        assertDoesNotThrow(() -> btSignal.BTbroadcast(),
                "La conexión Bluetooth debe estar disponible y no lanzar excepción.");
    }

    // Nuevo test: Verificar conexión Bluetooth no disponible
    @Test
    void testBTbroadcast_BluetoothNotAvailable() {
        // Simulamos Bluetooth no disponible
        btSignal = new SimulatedUnbondedBTSignal(false);

        // Llamamos al método BTbroadcast y verificamos que lanza ConnectException
        assertThrows(ConnectException.class, () -> btSignal.BTbroadcast(),
                "Se esperaba una excepción ConnectException para Bluetooth no disponible.");
    }


}
