package services.smartfeatures;

import data.VehicleID;
import exceptions.CorruptedImgException;

import java.awt.image.BufferedImage;

public class SimulatedQRDecoder implements QRDecoder {
    public VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException {
        if (QRImg == null) {
            throw new CorruptedImgException("Imagen QR nula o corrupta");
        }
        // Simulamos que el código QR "qr_vehicle1.png" corresponde al vehículo con ID "V123"
        return new VehicleID("V123");
    }

    public VehicleID getVehicleIDByImg(String imageName) throws CorruptedImgException {
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new CorruptedImgException("Nombre de archivo nulo o vacío");
        }
        // Simulamos que "qr_vehicle1.png" corresponde a "V123", y otros códigos no los reconoce
        if (imageName.equals("qr_vehicle1.png")) {
            return new VehicleID("V123");
        }
        throw new CorruptedImgException("Código QR no reconocido");
    }
}

