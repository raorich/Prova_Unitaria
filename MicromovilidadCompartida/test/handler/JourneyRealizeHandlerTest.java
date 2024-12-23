package handler;

import exceptions.ConnectException;
import exceptions.CorruptedImgException;
import exceptions.PMVNotAvailException;
import handler.JourneyRealizeHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JourneyRealizeHandlerTest {

    JourneyRealizeHandler handler = new JourneyRealizeHandler();

    @Test
    void testScanQR_invalidImage() {
        String invalidImagePath = "path_to_invalid_image.png";

        // Esperar que se lance una excepción CorruptedImgException
        assertThrows(CorruptedImgException.class, () -> {
            handler.scanQR(invalidImagePath, "user123", "station456");
        });
    }

    @Test
    void testScanQR_vehicleNotAvailable() {
        String qrPath = "path_to_qr_with_vehicle_not_available.png";

        // Esperar que se lance una excepción PMVNotAvailException
        assertThrows(PMVNotAvailException.class, () -> {
            handler.scanQR(qrPath, "user123", "station456");
        });
    }

    @Test
    void testScanQR_connectionError() {
        String qrPath = "path_to_qr_with_connection_error.png";

        // Esperar que se lance una excepción ConnectException
        assertThrows(ConnectException.class, () -> {
            handler.scanQR(qrPath, "user123", "station456");
        });
    }

    @Test
    void testScanQR_validScenario() {
        String qrPath = "path_to_valid_qr_image.png";

        // Si todo es correcto, el emparejamiento debe completarse sin excepciones
        assertDoesNotThrow(() -> {
            handler.scanQR(qrPath, "user123", "station456");
        });
    }
}
