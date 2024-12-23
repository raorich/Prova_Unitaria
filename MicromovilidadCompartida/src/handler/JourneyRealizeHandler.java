package handler;

import exceptions.*;
import services.*;
import services.smartfeatures.*;
import micromobility.*;
import domain.*;
import data.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class JourneyRealizeHandler {

    private Server server;
    private UnbondedBTSignal btSignal;
    private PMVehicle pmVehicle;
    private JourneyService journeyService;
    private QRDecoder qrDecoder;

    public JourneyRealizeHandler(Server server, UnbondedBTSignal btSignal, PMVehicle pmVehicle, JourneyService journeyService, QRDecoder qrDecoder) {
        this.server = server;
        this.btSignal = btSignal;
        this.pmVehicle = pmVehicle;
        this.journeyService = journeyService;
        this.qrDecoder = this.qrDecoder;
    }

    public String broadcastStationID(String statID) throws ConnectException {
        if (statID == null || statID.isEmpty()) {
            throw new ConnectException("No se puede recibir la señal de la estación.");
        }
        System.out.println("ID de estación recibido: " + statID);
        return statID;
    }


    public void scanQR(String imagePath) throws ConnectException, InvalidPairingArgsException, PMVNotAvailException, CorruptedImgException {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new InvalidPairingArgsException("Datos del QR inválidos.");
        }

        try {
            // Generar el VehicleID utilizando el hashCode del qrData
            BufferedImage qrImage = loadImageFromPath(imagePath);
            VehicleID vehID = qrDecoder.getVehicleID(qrImage);

            // Verificar la disponibilidad del vehículo
            server.checkPMVAvail(vehID);

            // Crear una nueva instancia de JourneyService
            journeyService = new JourneyService();
            journeyService.setServiceInit(LocalDateTime.now());

            // Actualizar valores en JourneyService
            //journeyService.setOriginPoint(currentLocation); // Se debe obtener la ubicación actual
            //journeyService.setOriginStationID(originStationID); // Identificar la estación de origen

            // Establecer conexión Bluetooth
            pmVehicle.setNotAvailb(); // Cambiar el estado del vehículo a NotAvailable
            System.out.println("Vehículo vinculado y listo para iniciar desplazamiento.");
        } catch (PMVNotAvailException e) {
            throw new PMVNotAvailException("El vehículo no está disponible.");
        } catch (ConnectException e) {
            throw new ConnectException("Error de conexión con el servidor o Bluetooth.");
        } catch (CorruptedImgException e) {
            throw e;  // Aquí es donde modificamos para lanzar directamente la excepción CorruptedImgException
        }
    }

    // Método para cargar la imagen
    private BufferedImage loadImageFromPath(String imagePath) throws CorruptedImgException {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            throw new CorruptedImgException("No se pudo cargar la imagen desde la ruta: " + imagePath);
        }
    }

    public void startDriving() throws ConnectException {
        pmVehicle.setUnderWay(); // Cambiar el estado del vehículo
        journeyService.setServiceInit(LocalDateTime.now()); // Iniciar el trayecto
        System.out.println("Trayecto iniciado.");
    }

    public void stopDriving() throws ConnectException {
        pmVehicle.setAvailb(); // Cambiar el estado del vehículo
        journeyService.setServiceFinish(LocalDateTime.now(), 10.5f, 30, 21.0f, new BigDecimal("15.00")); // Finalizar el trayecto
        System.out.println("Trayecto finalizado.");
    }

    // Métodos internos para cálculos
    private void calculateValues(double distance, int duration, float avgSpeed) {
        // Aquí iría la lógica para calcular valores del trayecto
        System.out.println("Calculando valores del trayecto...");
    }

    private void calculateImport(float distance, int duration, float avgSpeed) {
        // Aquí iría la lógica para calcular el importe del servicio
        System.out.println("Calculando el importe...");
    }
}
