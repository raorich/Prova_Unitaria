package services.smartfeatures;

import java.awt.image.BufferedImage;
import exceptions.CorruptedImgException;
import data.VehicleID;
import java.io.File;


public class SimulatedQRDecoder implements QRDecoder {

    @Override
    public VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException {
        // Simulamos la decodificación del QR en función del nombre del archivo

        // Si la imagen es "qr_vehicle1.png", devolvemos el VehicleID con ID "V123"
        // Extraemos el nombre del archivo de la ruta
        String fileName = new File("qr_vehicle1.png").getName();

        if (fileName.equals("qr_vehicle1.png")) {
            return new VehicleID("V123");  // ID válido
        }

        // Si la imagen es "qr_invalid.png", lanzamos la excepción
        if (fileName.equals("qr_invalid.png")) {
            throw new CorruptedImgException("Imagen QR no válida.");
        }

        // Si la imagen no corresponde a ningún caso, lanzamos una excepción de imagen corrupta
        throw new CorruptedImgException("Imagen QR no válida.");
    }
}
