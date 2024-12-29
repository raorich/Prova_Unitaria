package services.smartfeatures;

import data.*;
import micromobility.JourneyService;
import micromobility.PMVehicle;
import exceptions.ConnectException;
import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;

public class SimulatedArduinoMicroController implements ArduinoMicroController {

    private boolean btConnected;
    private boolean vehicleAvailable;
    private PMVehicle pmVehicle;
    private JourneyService journeyService;
    private StationID station;
    private GeographicPoint location;
    private UserAccount user;

    // Constructor
    public SimulatedArduinoMicroController(boolean btConnected, boolean vehicleAvailable, PMVehicle pmVehicle,
                                           JourneyService journeyService, StationID station, GeographicPoint location, UserAccount user) {
        this.btConnected = btConnected;
        this.vehicleAvailable = vehicleAvailable;
        this.pmVehicle = pmVehicle;
        this.journeyService = journeyService;
        this.station = station;
        this.location = location;
        this.user = user;
    }

    @Override
    public void setBTconnection() throws ConnectException {
        if (!btConnected) {
            throw new ConnectException("No se puede establecer la conexión Bluetooth. El dispositivo no está disponible.");
        }
        System.out.println("Conexión Bluetooth establecida con éxito.");
    }
    @Override
    public void startDriving() throws PMVPhisicalException, ProceduralException, ConnectException {
        //precondiciones:
        if (!btConnected) {
            throw new ConnectException("La conexión Bluetooth no está establecida.");
        }
        if (pmVehicle.getState() != PMVehicle.PMVState.NotAvailable) {
            throw new ProceduralException("El vehículo no está en el estado correcto para iniciar el trayecto. Debe estar 'NotAvailable'.");
        }
        // Simulamos un 5% de probabilidad de que haya un problema técnico en el vehículo
        double random = Math.random();
        if (random < 0.05) {
            throw new PMVPhisicalException("Error técnico en el vehículo, no puede iniciar el trayecto.");
        }

        pmVehicle.setUnderWay();
        System.out.println("El trayecto ha comenzado con el vehículo: " + pmVehicle.getLocation());

        // Actualizar el estado de JourneyService
        journeyService.setServiceInit(java.time.LocalDateTime.now());
        journeyService.setOriginPoint(location);
        journeyService.setOrgStatID(station);
        journeyService.setUserAccount(user);

        journeyService.setInProgress(true);

        System.out.println("Trayecto iniciado. Ubicación de inicio: " + location);
    }

    @Override
    public void stopDriving() throws ConnectException, PMVPhisicalException, ProceduralException {
        // Verificación de precondiciones:
        if (!btConnected) {
            throw new ConnectException("La conexión Bluetooth no está establecida.");
        }
        if (pmVehicle.getState() != PMVehicle.PMVState.UnderWay) {
            throw new ProceduralException("El vehículo no está en el estado correcto para detenerlo. Debe estar 'UnderWay'.");
        }
        if (!journeyService.isInProgress()) {
            throw new ProceduralException("El trayecto no está en curso. No se puede detener el vehículo.");
        }
        try {
            System.out.println("Deteniendo el vehículo...");
        } catch (Exception e) {
            throw new ConnectException("Error en la conexión Bluetooth al intentar detener el vehículo: " + e.getMessage());
        }
        System.out.println("El vehículo ha sido detenido con éxito. El trayecto ha terminado.");
    }

    @Override
    public void undoBTconnection() {
        btConnected = false; // Desconectar el Bluetooth
        System.out.println("Conexión Bluetooth desconectada.");
    }
}
