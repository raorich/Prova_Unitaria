package services;
import domain.JourneyService;
import exceptions.*;
import data.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Server {// External service for the persistent storage
    /**
     * External services involved in the shared micromobility system
     */

        // To be invoked by the use case controller
        void checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException;
        void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) throws InvalidPairingArgsException, ConnectException;
        void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date, float avSp, float dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException;
        // Internal operations
        void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date);
        void unPairRegisterService(JourneyService s) throws PairingNotFoundException;
        void registerLocation(VehicleID veh, StationID st);

}
