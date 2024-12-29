package services;

import data.*;
import micromobility.JourneyService;
import exceptions.*;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.SimulatedQRDecoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerSimulated implements Server {
    private final QRDecoder qrDecoder;
    private final List<JourneyService> journeyCache; // Caché de los servicios de trayecto

    public ServerSimulated() {
        this.qrDecoder = new SimulatedQRDecoder();
        this.journeyCache = new ArrayList<>();
    }

    @Override
    public QRDecoder getQRDecoder() {
        return qrDecoder;
    }

    @Override
    public void checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException {
        if (!"V123".equals(vhID.getVehicleId())) {
            throw new PMVNotAvailException("El vehículo no está disponible o ya está emparejado.");
        }
        System.out.println("El vehículo está disponible.");
    }

    @Override
    public void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date)
            throws InvalidPairingArgsException, ConnectException {
        if (user == null || veh == null || st == null || loc == null || date == null) {
            throw new InvalidPairingArgsException("Argumentos inválidos para registrar el emparejamiento.");
        }
        System.out.println("Emparejamiento registrado exitosamente en el servidor.");
        setPairing(user,veh,st,loc,date);
    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date,
                            float avSp, float dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException {
        if (user == null || veh == null || st == null || loc == null || date == null) {
            throw new InvalidPairingArgsException("Argumentos inválidos para finalizar el emparejamiento.");
        }
        System.out.println("Emparejamiento finalizado y registrado en el servidor.");
        registerLocation(veh,st);
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {
        System.out.println("Se puede iniciar trayecto");
    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {
        if (s == null) {
            throw new PairingNotFoundException("No se encontró la información del emparejamiento en el servidor.");
        }
        if (s.getVehicleID() == null || s.getUserAccount() == null) {
            throw new PairingNotFoundException("No se encontró información suficiente para el emparejamiento.");
        }
        try {
            // Actualizar el estado del emparejamiento en la base de datos
            System.out.println("Actualizando emparejamiento para el vehículo " + s.getVehicleID().getVehicleId());
            System.out.println("Usuario: " + s.getUserAccount().getAccountId());
            System.out.println("Estación: " + s.getOrgStatID().getId());
            System.out.println("Ubicación: " + s.getOriginPoint());
            System.out.println("Duración del trayecto: " + s.getDuration() + " minutos");
            System.out.println("Distancia recorrida: " + s.getDistance() + " metros");
            System.out.println("Importe del servicio: " + s.getImportAmount());

            System.out.println("Servicio registrado correctamente en el servidor.");

        } catch (Exception e) {
            // Manejo de excepciones genéricas
            throw new PairingNotFoundException("Hubo un error al intentar registrar el servicio: " + e.getMessage());
        }
    }

    @Override
    public void registerLocation(VehicleID veh, StationID st) {
        System.out.println("Ubicación del vehículo registrada en el servidor.");
    }
    private boolean isConnected() {
        return Math.random() > 0.2; // Simulamos un 80% de éxito en la conexión
    }
    @Override
    public void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws ConnectException {
        // Verificamos la conexión
        if (!isConnected()) {
            throw new ConnectException("Unable to connect to the server.");
        }

        // Imprimir el detalle del pago registrado simulando el registro
        System.out.println("Pago registrado en el servidor:");
        System.out.println("Usuario: " + user.getAccountId());
        System.out.println("Importe: " + imp);
        System.out.println("Método de pago: " + (payMeth == 'W' ? "Monedero" : "Otro método"));

        JourneyService journeyService = new JourneyService(servID, user, imp, payMeth);
        journeyCache.add(journeyService);

        System.out.println("Pago registrado correctamente.");
    }

    public void removeJourneyFromCache(JourneyService s) {
        if (s == null) {
            System.out.println("El JourneyService proporcionado es nulo.");
            return;
        }

        // Intentamos eliminar el JourneyService del caché
        boolean removed = journeyCache.remove(s);

        if (removed) {
            System.out.println("JourneyService ha sido eliminado del caché.");
        } else {
            System.out.println("No se encontró el JourneyService en el caché.");
        }
    }
    public void addJourneyToCache(JourneyService s) {
        journeyCache.add(s);
        System.out.println("JourneyService ha sido agregado al caché.");
    }
}
