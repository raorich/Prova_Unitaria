package domain;

import data.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JourneyService {
    private LocalDateTime initDate;   // Fecha y hora de inicio
    private LocalDateTime endDate;    // Fecha y hora de fin
    private boolean inProgress;       // Estado del trayecto (en progreso o no)
    private float distance;           // Distancia recorrida en metros
    private int duration;             // Duración del trayecto en minutos
    private float avgSpeed;           // Velocidad promedio en km/h
    private BigDecimal importAmount;  // Importe a pagar por el servicio

    // Nuevos atributos
    private StationID orgStatID;      // Estación de origen
    private GeographicPoint originPoint; // Ubicación inicial
    private GeographicPoint endPoint;   // Ubicación final
    private UserAccount userAccount;    // Cuenta del usuario
    private VehicleID vehicleID;       // ID del vehículo
    private ServiceID serviceID; // Nuevo atributo para el ServiceID

    // Constructor con parámetros
    public JourneyService(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) {
        // Llenamos los valores con los parámetros que recibimos
        this.initDate = LocalDateTime.now();  // Se asume que el trayecto inicia al crear el objeto
        this.inProgress = true;               // El trayecto comienza en progreso
        this.importAmount = imp;
        this.userAccount = user;
        this.serviceID = serviceID; // Asignamos el ServiceID
        // Asumimos que el resto de los atributos podrían ser establecidos en otro momento
    }

    // Métodos para inicialización y finalización del servicio
    public void setServiceInit(LocalDateTime initDate) {
        if (initDate == null) throw new IllegalArgumentException("La fecha de inicio no puede ser nula.");
        this.initDate = initDate;
        this.inProgress = true;
    }

    public void setServiceFinish(LocalDateTime endDate) {
        if (endDate == null || endDate.isBefore(this.initDate)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        this.endDate = endDate;
        this.inProgress = false;
    }

    public LocalDateTime getServiceInit() {
        return this.initDate;
    }

    public LocalDateTime getServiceFinish() {
        return this.endDate;
    }

    // Métodos para distancia, duración y velocidad promedio
    public void setDistance(float distance) {
        if (distance < 0) throw new IllegalArgumentException("La distancia no puede ser negativa.");
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setDuration(int duration) {
        if (duration < 0) throw new IllegalArgumentException("La duración no puede ser negativa.");
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setAvgSpeed(float avgSpeed) {
        if (avgSpeed < 0) throw new IllegalArgumentException("La velocidad promedio no puede ser negativa.");
        this.avgSpeed = avgSpeed;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setImportAmount(BigDecimal importAmount) {
        if (importAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo.");
        }
        this.importAmount = importAmount;
    }

    public BigDecimal getImportAmount() {
        return importAmount;
    }

    // Métodos para otros atributos
    public void setOrgStatID(StationID orgStatID) {
        this.orgStatID = orgStatID;
    }

    public StationID getOrgStatID() {
        return orgStatID;
    }

    public void setOriginPoint(GeographicPoint originPoint) {
        this.originPoint = originPoint;
    }

    public GeographicPoint getOriginPoint() {
        return originPoint;
    }

    public void setEndPoint(GeographicPoint endPoint) {
        this.endPoint = endPoint;
    }

    public GeographicPoint getEndPoint() {
        return endPoint;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setVehicleID(VehicleID vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleID getVehicleID() {
        return vehicleID;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setServiceID(ServiceID serviceID) {
        this.serviceID = serviceID;
    }

    public ServiceID getServiceID() {
        return this.serviceID;
    }
}
