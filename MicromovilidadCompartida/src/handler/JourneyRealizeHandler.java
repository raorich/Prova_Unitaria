package handler;

import exceptions.*;
import services.*;
import services.smartfeatures.*;
import micromobility.*;
import domain.*;
import data.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class JourneyRealizeHandler {

    private Server server;
    private UnbondedBTSignal btSignal;
    private PMVehicle pmVehicle;
    private JourneyService journeyService;

    public JourneyRealizeHandler(Server server, UnbondedBTSignal btSignal, PMVehicle pmVehicle, JourneyService journeyService) {
        this.server = server;
        this.btSignal = btSignal;
        this.pmVehicle = pmVehicle;
        this.journeyService = journeyService;
    }

    public String broadcastStationID(String statID) throws ConnectException {
        if (statID == null || statID.isEmpty()) {
            throw new ConnectException("No se puede recibir la señal de la estación.");
        }
        System.out.println("ID de estación recibido: " + statID);
        return statID;
    }

    public void scanQR(String qrData) throws ConnectException, InvalidPairingArgsException, PMVNotAvailException {
        if (qrData == null || qrData.isEmpty()) {
            throw new InvalidPairingArgsException("Datos del QR inválidos.");
        }

        try {
            // Generar el VehicleID utilizando el hashCode del qrData
            VehicleID vehID = new VehicleID(String.valueOf(qrData.hashCode()));

            // Verificar la disponibilidad del vehículo
            server.checkPMVAvail(vehID);

            // Crear una nueva instancia de JourneyService
            journeyService = new JourneyService();
            journeyService.setServiceInit(LocalDateTime.now());

            // Actualizar valores en JourneyService
            journeyService.setOriginPoint(currentLocation); // Se debe obtener la ubicación actual
            journeyService.setOriginStationID(originStationID); // Identificar la estación de origen

            // Establecer conexión Bluetooth
            pmVehicle.setNotAvailb(); // Cambiar el estado del vehículo a NotAvailable
            System.out.println("Vehículo vinculado y listo para iniciar desplazamiento.");

            // Registrar la vinculación en el servidor
            server.registerPairing(currentUser, vehID, originStationID, currentLocation, LocalDateTime.now());
        } catch (InvalidPairingArgsException e) {
            throw new InvalidPairingArgsException("Error en los argumentos al registrar la vinculación.");
        } catch (PMVNotAvailException e) {
            throw new PMVNotAvailException("El vehículo no está disponible.");
        } catch (ConnectException e) {
            throw new ConnectException("Error de conexión con el servidor o Bluetooth.");
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
