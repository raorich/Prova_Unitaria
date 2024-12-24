package services.smartfeatures;
import java.awt.image.BufferedImage;
import exceptions.*;
import data.*;

public interface QRDecoder {
        // Método para decodificar QR desde una imagen
        VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException;

        // Método adicional para simular la decodificación desde el nombre del archivo
        VehicleID getVehicleIDByImg(String imageName) throws CorruptedImgException;
}
