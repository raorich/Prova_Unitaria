package handler;

import exceptions.ConnectException;
import services.Server;
import services.smartfeatures.UnbondedBTSignal;

public class JourneyRealizeHandler {

    private Server server;
    private UnbondedBTSignal btSignal;


    public String broadcastStationID(String statID) throws ConnectException {
        if (statID == null || statID.isEmpty()) {
            throw new ConnectException("No se puede recibir la señal de la estación.");
        }
        System.out.println("ID de estación recibido: " + statID);
        return statID;
    }

    public void scanQR(String qrData) throws ConnectException {
        if (qrData == null || qrData.isEmpty()) {
            throw new ConnectException("Error al escanear el QR.");
        }
        System.out.println("QR escaneado correctamente: " + qrData);
    }

    public void startDriving() throws ConnectException {
        System.out.println("Desplazamiento iniciado.");
    }

    public void stopDriving() throws ConnectException {
        System.out.println("Desplazamiento detenido.");
    }

    public void unPairVehicle() throws ConnectException {
        System.out.println("Vehículo desemparejado correctamente.");
    }
}
