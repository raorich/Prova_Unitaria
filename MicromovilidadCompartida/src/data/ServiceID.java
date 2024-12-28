package data;

import java.util.UUID;

public final class ServiceID {
    private final String serviceId;

    // Constructor que genera un UUID único
    public ServiceID() {
        this.serviceId = UUID.randomUUID().toString();  // Asignar un UUID único como identificador
    }

    // Si deseas que el ServiceID pueda recibir un valor como parámetro:
    public ServiceID(String serviceId) {
        if (serviceId == null || serviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("ServiceID cannot be null or empty");
        }
        if (!serviceId.matches("[a-zA-Z0-9]{3,36}")) {
            throw new IllegalArgumentException("ServiceID must be alphanumeric and 3-36 characters long");
        }
        this.serviceId = serviceId;
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
