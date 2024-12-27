package services.smartfeatures;

import domain.*;
import data.*;
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

    // Simula la conexión Bluetooth
    @Override
    public void setBTconnection() throws ConnectException {
        if (!btConnected) {
            throw new ConnectException("No se puede establecer la conexión Bluetooth. El dispositivo no está disponible.");
        }
        System.out.println("Conexión Bluetooth establecida con éxito.");
    }

    // Simula el inicio del trayecto
    @Override
    public void startDriving() throws PMVPhisicalException, ProceduralException, ConnectException {
        // Verificación de precondiciones:
        if (!btConnected) {
            throw new ConnectException("La conexión Bluetooth no está establecida.");
        }

        if (!vehicleAvailable) {
            throw new PMVPhisicalException("El vehículo no está disponible para conducir.");
        }

        if (pmVehicle.getState() != PMVehicle.PMVState.NotAvailable) {
            throw new ProceduralException("El vehículo no está en el estado correcto para iniciar el trayecto. Debe estar 'NotAvailable'.");
        }

        // Cambiar el estado del vehículo a 'UnderWay' (en marcha)
        pmVehicle.setUnderWay();
        System.out.println("El trayecto ha comenzado con el vehículo: " + pmVehicle.getLocation());

        // Configurar el viaje (actualizar el estado de JourneyService)
        journeyService.setServiceInit(java.time.LocalDateTime.now()); // Iniciar el trayecto con la hora actual
        journeyService.setOriginPoint(location); // Establecer la ubicación del conductor
        journeyService.setOrgStatID(station); // Establecer la estación de origen
        journeyService.setUserAccount(user); // Asociar la cuenta de usuario

        // Establecer que el trayecto está en progreso
        journeyService.setInProgress(true);

        System.out.println("Trayecto iniciado. Ubicación de inicio: " + location);
    }

    // Método para simular la detención del trayecto
    @Override
    public void stopDriving() throws ConnectException, PMVPhisicalException, ProceduralException {
        // Verificación de precondiciones:

        // Comprobar si la conexión Bluetooth está activa
        if (!btConnected) {
            throw new ConnectException("La conexión Bluetooth no está establecida.");
        }

        // Comprobar si el vehículo está en estado 'UnderWay'
        if (pmVehicle.getState() != PMVehicle.PMVState.UnderWay) {
            throw new ProceduralException("El vehículo no está en el estado correcto para detenerlo. Debe estar 'UnderWay'.");
        }

        // Comprobar si el trayecto está en progreso
        if (!journeyService.isInProgress()) {
            throw new ProceduralException("El trayecto no está en curso. No se puede detener el vehículo.");
        }
        // Intentamos detener el vehículo (esto puede ser simulado o a través de algún mecanismo físico)
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
