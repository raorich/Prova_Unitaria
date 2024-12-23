package services.smartfeatures;
import java.awt.image.BufferedImage;
import exceptions.*;
import data.*;

public interface QRDecoder {
        VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException;

}
