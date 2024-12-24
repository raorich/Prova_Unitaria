package services;

import data.*;
import domain.JourneyService;
import exceptions.*;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.SimulatedQRDecoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServerSimulated implements Server {
    private final QRDecoder qrDecoder;

    public ServerSimulated() {
        this.qrDecoder = new SimulatedQRDecoder();
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
    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date,
                            float avSp, float dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException {
        if (user == null || veh == null || st == null || loc == null || date == null) {
            throw new InvalidPairingArgsException("Argumentos inválidos para finalizar el emparejamiento.");
        }
        System.out.println("Emparejamiento finalizado y registrado en el servidor.");
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {
        System.out.println("Datos de emparejamiento persistidos.");
    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {
        if (s == null) {
            throw new PairingNotFoundException("No se encontró la información del emparejamiento en el servidor.");
        }
        System.out.println("Servicio registrado correctamente.");
    }

    @Override
    public void registerLocation(VehicleID veh, StationID st) {
        System.out.println("Ubicación del vehículo registrada en el servidor.");
    }

    @Override
    public void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws ConnectException {
        System.out.println("Pago registrado en el servidor.");
    }
}
