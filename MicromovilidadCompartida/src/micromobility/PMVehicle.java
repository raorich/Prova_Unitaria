package micromobility;
import data.*;

public class PMVehicle {
    public enum PMVState {
        Available, NotAvailable, UnderWay, TemporaryParking
    }
    private PMVState state;
    private GeographicPoint location;

    public PMVehicle() {
        this.state = PMVState.Available; // Estado inicial
    }

    public PMVState getState() {
        return state;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public void setNotAvailb() {
        this.state = PMVState.NotAvailable;
    }

    public void setUnderWay() {
        this.state = PMVState.UnderWay;
    }

    public void setAvailb() {
        this.state = PMVState.Available;
    }

    public void setLocation(GeographicPoint gP) {
        this.location = gP;
    }
}


