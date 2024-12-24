package handler;

import data.VehicleID;
import exceptions.CorruptedImgException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.smartfeatures.SimulatedQRDecoder;

import static org.junit.jupiter.api.Assertions.*;

class SimulatedQRDecoderTest {

    private SimulatedQRDecoder qrDecoder;

    @BeforeEach
    void setUp() {
        // Inicializamos el QRDecoder
        qrDecoder = new SimulatedQRDecoder();
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
}
