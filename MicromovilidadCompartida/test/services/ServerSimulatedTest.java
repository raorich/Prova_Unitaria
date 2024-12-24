package services;

import data.VehicleID;
import exceptions.CorruptedImgException;
import org.junit.jupiter.api.Test;
import services.smartfeatures.QRDecoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerSimulatedTest {
    @Test
    void testInvalidQRCode() {
        ServerSimulated server = new ServerSimulated();
        QRDecoder qrDecoder = server.getQRDecoder();

        assertThrows(CorruptedImgException.class, () -> qrDecoder.getVehicleIDByImg("qr_invalid.png"));
    }
}
