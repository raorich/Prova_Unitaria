package data;

public final class VehicleID {
    private final String vehicleId;

    public VehicleID(String vehicleId) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("VehicleID cannot be null or empty");
        }
        if (!vehicleId.matches("[a-zA-Z0-9]{3,10}")) {
            throw new IllegalArgumentException("VehicleID must be alphanumeric and 3-10 characters long");
        }
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleID vehicleID = (VehicleID) o;
        return vehicleId.equals(vehicleID.vehicleId);
    }

    @Override
    public int hashCode() {
        return vehicleId.hashCode();
    }

    @Override
    public String toString() {
        return "VehicleID{" + "vehicleId='" + vehicleId + '\'' + '}';
    }
}
