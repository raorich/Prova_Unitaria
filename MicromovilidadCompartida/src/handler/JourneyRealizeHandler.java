package handler;

import exceptions.*;
import services.*;
import services.smartfeatures.*;
import micromobility.*;
import domain.*;
import data.*;

import java.io.IOException;
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
        this.qrDecoder = qrDecoder; // Cambiado para evitar `null`
    }


    public String broadcastStationID(String statID) throws ConnectException {
        if (statID == null || statID.isEmpty()) {
            throw new ConnectException("No se puede recibir la señal de la estación.");
        }
        System.out.println("ID de estación recibido: " + statID);
        return statID;
    }


    public void scanQR(String imageName) throws ConnectException, InvalidPairingArgsException, PMVNotAvailException, CorruptedImgException {
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new InvalidPairingArgsException("El nombre del archivo no puede ser nulo o vacío.");
        }

        try {
            // Usar el nombre del archivo para obtener el ID del vehículo
            VehicleID vehID = qrDecoder.getVehicleIDByImg(imageName);

            // Verificar disponibilidad del vehículo
            server.checkPMVAvail(vehID);

            // Completar flujo
            System.out.println("Emparejamiento completado con el vehículo: " + vehID.getVehicleId());
        } catch (PMVNotAvailException | CorruptedImgException | ConnectException e) {
            throw e;
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
