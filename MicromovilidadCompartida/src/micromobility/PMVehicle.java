package micromobility;
import data.*;
import exceptions.ProceduralException;

public class PMVehicle {
    public enum PMVState {
        Available, NotAvailable, UnderWay, TemporaryParking
    }

    private PMVState state;
    private GeographicPoint location;
    private VehicleID vehicleID;  // Campo para almacenar el VehicleID

    public PMVehicle(VehicleID vehicleID) {
        this.state = PMVState.Available; // Estado inicial
        this.vehicleID = vehicleID;  // Inicializamos el VehicleID
    }

    public PMVState getState() {
        return state;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public void setLocation(GeographicPoint gP) {
        this.location = gP;
    }

    public void setNotAvailb() throws ProceduralException {
        if (state == PMVState.UnderWay) {
            throw new ProceduralException("No se puede marcar el vehículo como no disponible mientras está en uso.");
        }
        this.state = PMVState.NotAvailable;
    }

    public void setUnderWay() throws ProceduralException {
        if (state == PMVState.Available) {
            throw new ProceduralException("El vehículo no está no esta emparejado.");
        }
        this.state = PMVState.UnderWay;
    }

    public void setAvailb() throws ProceduralException {
        if (state == PMVState.UnderWay) {
            throw new ProceduralException("No se puede marcar el vehículo como disponible mientras está en uso.");
        }
        this.state = PMVState.Available;
    }

    // Nuevo método para obtener el VehicleID
    public VehicleID getVehicleID() {
        return vehicleID;
    }
}


