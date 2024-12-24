package data;

public final class ServiceID {
    private final String serviceId;

    public ServiceID(String serviceId) {
        if (serviceId == null || serviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("ServiceID cannot be null or empty");
        }
        if (!serviceId.matches("[a-zA-Z0-9]{3,10}")) {
            throw new IllegalArgumentException("VehicleID must be alphanumeric and 3-10 characters long");
        }
        this.vehicleId = vehicleId;
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceID serviceID = (ServiceID) o;
        return serviceId.equals(serviceID.serviceId);
    }

    @Override
    public int hashCode() {
        return serviceId.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceID{" + "serviceId='" + serviceId + '\'' + '}';
    }
}